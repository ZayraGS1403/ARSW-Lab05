package edu.eci.arsw.blueprints.controller;

import edu.eci.arsw.blueprints.model.*;
import edu.eci.arsw.blueprints.persistence.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import edu.eci.arsw.blueprints.services.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;


/**
 * Controlador REST para gestionar los blueprints.
 */
@RestController
@RequestMapping( "/blueprints")
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
    public ResponseEntity<?> registerBlueprint(@RequestBody Blueprint bp){
        HashMap<String, Object> response = new HashMap<>();
        try {
            bps.addNewBlueprint(bp);
            response.put("status", "success");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * Obtiene todos los blueprints disponibles en el sistema.
     * @return Conjunto de blueprints.
     */
    @GetMapping
    public Set<Blueprint> getAllBlueprints(){
        return bps.getAllBlueprints();
    }
}
