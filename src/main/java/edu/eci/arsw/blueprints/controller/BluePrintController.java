package edu.eci.arsw.blueprints.controller;

import edu.eci.arsw.blueprints.model.*;
import edu.eci.arsw.blueprints.persistence.*;
import edu.eci.arsw.blueprints.services.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * Controlador REST para gestionar los blueprints.
 */
@RestController
@RequestMapping( "/blueprints")
@Tag(name = "Blueprints", description = "API para gestionar blueprints")
public class BluePrintController {

    @Autowired
    private final BlueprintsServices bps;

    /**
     * Constructor de BluePrintController.
     * @param bps Servicio de blueprints inyectado.
     */
    public BluePrintController(BlueprintsServices bps) {
        this.bps = bps;
    }

    /**
     * Obtiene un blueprint específico por autor y nombre.
     * @param author Nombre del autor del blueprint.
     * @param name Nombre del blueprint.
     * @return ResponseEntity con el blueprint solicitado o un error si no se encuentra.
     */
    @GetMapping("/{author}/{name}")
    @Operation(summary = "Obtener un blueprint por autor y nombre", description = "Devuelve un blueprint basado en su autor y nombre.")
    public ResponseEntity<?> getBlueprint(@PathVariable("author") String author, @PathVariable("name") String name) {
        try {
            return ResponseEntity.ok(bps.getBlueprint(author, name));
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Blueprint not found: " + author + "/" + name);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }

    /**
     * Obtiene todos los blueprints de un autor específico.
     * @param author Nombre del autor.
     * @return ResponseEntity con el conjunto de blueprints o un error si no se encuentran.
     */
    @GetMapping("/{author}")
    @Operation(summary = "Obtener todos los blueprints de un autor", description = "Devuelve una lista de blueprints pertenecientes a un autor específico.")
    public ResponseEntity<?> getBlueprintByAuthor(@PathVariable("author") String author) {
        try {
            Set<Blueprint> blueprints = bps.getBlueprintsByAuthor(author);
            return ResponseEntity.ok(blueprints);
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Blueprints not found for author: " + author);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }

    /**
     * Registra un nuevo blueprint en el sistema.
     * @param bp Objeto Blueprint a registrar.
     * @return ResponseEntity con el estado de la operación.
     */
    @PostMapping
    @Operation(summary = "Registrar un nuevo blueprint", description = "Añade un nuevo blueprint al sistema.")
    public ResponseEntity<?> registerBlueprint(@RequestBody Blueprint bp){
        HashMap<String, Object> response = new HashMap<>();
        try {
            bps.addNewBlueprint(bp);
            response.put("status", "success");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (BlueprintPersistenceException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * Obtiene todos los blueprints disponibles en el sistema.
     * @return Conjunto de blueprints.
     */
    @GetMapping
    @Operation(summary = "Obtener todos los blueprints", description = "Devuelve una lista de todos los blueprints registrados en el sistema.")
    public Set<Blueprint> getAllBlueprints(){
        return bps.getAllBlueprints();
    }

    /**
     * Actualiza un blueprint existente.
     * @param author Nombre del autor.
     * @param name Nombre del blueprint.
     * @param blueprint Blueprint actualizado.
     * @return ResponseEntity con el blueprint actualizado o un error si no se encuentra.
     */
    @PutMapping("/{author}/{name}")
    @Operation(summary = "Actualizar un blueprint", description = "Modifica los datos de un blueprint existente.")
    public ResponseEntity<?> updateBlueprint(@PathVariable("author") String author, @PathVariable("name") String name, @RequestBody Blueprint blueprint) {
        try {
            return ResponseEntity.ok(bps.updateBlueprint(author, name, blueprint));
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Blueprint not found: " + author + "/" + name);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }
}
