/*
 * 
 */
package de.science.hack;

import au.com.bytecode.opencsv.CSVReader;
import ch.lambdaj.group.Group;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Double.parseDouble;
import static java.lang.Math.abs;
import static ch.lambdaj.Lambda.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Mario
 */
public class WindDataReader {

    private CSVReader createReader(String name) throws FileNotFoundException {
        File file = new File(name);
        FileReader reader = new FileReader(file);
        return new CSVReader(reader, ',');
    }

    private List<String[]> readString(String name) {
        List<String[]> content = new ArrayList<>();
        try {
            CSVReader reader = createReader(name);
            content = reader.readAll();
        } catch (IOException ex) {
            Logger.getLogger(WindDataReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return content;
    }

    private Group<Coordinate> readCoordinates(String name) {
        List<Coordinate> coordinates = new ArrayList<>();
        List<String[]> content = readString(name);
        for (String[] cnt : content) {
            //we assume that the array length is 3
            double lon = parseDouble(cnt[0]);
            double lat = parseDouble(cnt[1]);
            double alt = abs(parseDouble(cnt[2]));
            coordinates.add(new Coordinate(lon, lat, alt));
        }
        return group(coordinates, by(on(Coordinate.class).getLon()));
    }

    public Map<String, List<StlPoint>> read(String name) {
        Map<String, List<StlPoint>> map = new HashMap<>();
        Group<Coordinate> group = readCoordinates(name);
        Set<String> keys = group.keySet();
        for (String key : keys) {
            List<StlPoint> stlCoordinates = new ArrayList<>();
            List<Coordinate> coordinates = group.find(key);
            for (Coordinate coord : coordinates) {
                Coordinate groundCoord = new Coordinate(coord.getLon(), coord.getLat(), 0);
                StlPoint coordForGround = CoordinatesConverter.toStl(groundCoord);
                stlCoordinates.add(coordForGround);
                StlPoint coordForWind = CoordinatesConverter.toStl(coord);
                stlCoordinates.add(coordForWind);
            }
            map.put(key, stlCoordinates);
        }

        return map;
    }
}
