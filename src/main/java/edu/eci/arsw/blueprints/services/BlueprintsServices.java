package edu.eci.arsw.blueprints.services;

import edu.eci.arsw.blueprints.filters.*;
import edu.eci.arsw.blueprints.model.*;
import edu.eci.arsw.blueprints.persistence.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio para la gestión de Blueprints.
 * Proporciona métodos para agregar, recuperar y filtrar planos.
 */
@Service
public class BlueprintsServices {
   
    @Autowired
    BlueprintsPersistence bpp;

    @Autowired
    private Filter filter;

    /**
     * Agrega un nuevo blueprint al sistema.
     * @param bp El blueprint a agregar.
     * @throws UnsupportedOperationException si el blueprint ya existe.
     */
    public void addNewBlueprint(Blueprint bp){
        try{
            bpp.saveBlueprint(bp);
        }catch (Exception ex){
            ex.printStackTrace();
            throw new UnsupportedOperationException("User already exists!");
        }
    }

    /**
     * Obtiene todos los blueprints disponibles, aplicando el filtro correspondiente.
     * @return Un conjunto de todos los blueprints filtrados.
     */
    public Set<Blueprint> getAllBlueprints(){
        return filter.filterBlueprints(bpp.getAllBluePrints());
    }

    /**
     * Obtiene un blueprint específico basado en el autor y el nombre.
     * @param author Autor del blueprint.
     * @param name Nombre del blueprint.
     * @return El blueprint filtrado.
     * @throws BlueprintNotFoundException si no se encuentra el blueprint.
     */
    public Blueprint getBlueprint(String author,String name) throws BlueprintNotFoundException{
        return filter.filterPlain(bpp.getBlueprint(author, name));
    }

    /**
     * Obtiene todos los blueprints de un autor específico.
     * @param author Autor de los blueprints.
     * @return Un conjunto de blueprints filtrados del autor.
     * @throws BlueprintNotFoundException si el autor no tiene blueprints.
     */
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException{
        return filter.filterBlueprints(bpp.getBlueprintsByAuthor(author));
    }

}
