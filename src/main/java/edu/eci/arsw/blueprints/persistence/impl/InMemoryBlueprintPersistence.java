package edu.eci.arsw.blueprints.persistence.impl;

import edu.eci.arsw.blueprints.model.*;
import edu.eci.arsw.blueprints.persistence.*;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Implementación en memoria de la persistencia de planos.
 * Almacena los planos en un mapa en memoria.
 */
@Service
public class InMemoryBlueprintPersistence implements BlueprintsPersistence{

    private final Map<Tuple<String,String>,Blueprint> blueprints=new HashMap<>();

    public InMemoryBlueprintPersistence() {
        //load stub data
        Point[] pts=new Point[]{new Point(140, 140),new Point(115, 115)};
        Blueprint bp=new Blueprint("_authorname_", "_bpname_ ",pts);
        blueprints.put(new Tuple<>(bp.getAuthor(),bp.getName()), bp);
        
    }

    /**
     * Guarda un nuevo blueprint en la memoria.
     * @param bp El blueprint a guardar.
     * @throws BlueprintPersistenceException si el blueprint ya existe.
     */
    @Override
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        String authorTrimmed = bp.getAuthor().trim();
        String nameTrimmed = bp.getName().trim();
        Tuple<String, String> key = new Tuple<>(authorTrimmed, nameTrimmed);

        if (blueprints.containsKey(key)) {
            throw new BlueprintPersistenceException("The given blueprint already exists: " + bp);
        } else {
            blueprints.put(key, bp);
        }
    }


    /**
     * Obtiene un blueprint por autor y nombre.
     * @param author Nombre del autor.
     * @param bprintname Nombre del blueprint.
     * @return El blueprint correspondiente.
     * @throws BlueprintNotFoundException si no se encuentra el blueprint.
     */
    @Override
    public Blueprint getBlueprint(String author, String bprintname) throws BlueprintNotFoundException {
        String authorTrimmed = author.trim();
        String nameTrimmed = bprintname.trim();
        Blueprint bp = blueprints.get(new Tuple<>(authorTrimmed, nameTrimmed));
        if (bp == null) {
            throw new BlueprintNotFoundException("Blueprint not found: " + author + " - " + bprintname);
        }
        return bp;
    }

    /**
     * Obtiene todos los blueprints de un autor específico.
     * @param author Nombre del autor.
     * @return Un conjunto de blueprints del autor.
     * @throws BlueprintNotFoundException si el autor no tiene blueprints.
     */
    @Override
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        String authorTrimmed = author.trim();
        Set<Blueprint> result = new HashSet<>();
        for (Map.Entry<Tuple<String, String>, Blueprint> entry : blueprints.entrySet()) {
            if (entry.getKey().getElem1().equals(authorTrimmed)) {
                result.add(entry.getValue());
            }
        }

        if (result.isEmpty()) {
            throw new BlueprintNotFoundException("No blueprints found for author: " + author);
        }
        return result;
    }

    /**
     * Obtiene todos los blueprints almacenados en memoria.
     * @return Un conjunto de todos los blueprints.
     */
    @Override
    public Set<Blueprint> getAllBluePrints(){
        Set<Blueprint> blueprintSet = new HashSet<>();

        for (Tuple<String, String> i : blueprints.keySet()){
            blueprintSet.add(blueprints.get(i));
        }

        return blueprintSet;
    }
    
}
