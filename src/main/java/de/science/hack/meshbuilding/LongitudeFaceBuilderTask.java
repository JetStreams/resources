/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version. 
 */
package de.science.hack.meshbuilding;

import de.science.hack.Line;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import toxi.geom.mesh.TriangleMesh;

/**
 * Adds faces to the mesh along the longitudes.
 * 
 * @author Mario
 */
class LongitudeFaceBuilderTask extends AbstractFaceBuilderTask {
    
    private SortedMap<Float, List<Line>> data;

    LongitudeFaceBuilderTask(SortedMap<Float, List<Line>> data) {
        this.data = data;
    }
    
    @Override
    protected TriangleMesh compute() {
        TriangleMesh mesh = new TriangleMesh();
        for (Map.Entry<Float, List<Line>> entry : data.entrySet()) {
            addFaces(mesh, entry.getValue());
        }
        return mesh;
    }
    
    /**
     * Adds faces to the mesh along the longitudes.
     *
     * @param lines
     * @param mesh
     */
    private void addFaces(TriangleMesh mesh, List<Line> lines) {
        for (int i = 0, m = lines.size(); i < m; i++) {
            //contains two projection or the four corners of a rectangle which is used to construct two triangles
            LinkedList<Line> tuple = nextTuple(lines, i);
            addFaces(mesh, tuple);
        }
    }
    
    //the next two projections
    private LinkedList<Line> nextTuple(List<Line> projections, int current) {
        LinkedList<Line> tuple = new LinkedList<>();
        if (current < projections.size() - 1) {
            tuple.add(projections.get(current));
            tuple.add(projections.get(current + 1));
        }
        return tuple;
    }
}