/*
 * 
 */
package de.science.hack;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import toxi.geom.AABB;
import toxi.geom.mesh.Mesh3D;

/**
 *
 * @author Mario
 */
public class ModelReaderTest {

    private ModelReader classUnderTest;

    @Before
    public void setUp() {
        classUnderTest = new ModelReader();
    }

    /**
     * Test of readEarth method, of class ModelReader.
     */
    @Test
    public void testReadEarth() {
        Mesh3D result = classUnderTest.readEarth();
        assertNotNull(result);
        assertEquals(489882, result.getNumFaces());
        AABB box = result.getBoundingBox();
        assertEquals(CoordinatesConverter.RADIUS, box.getMax().x, 0.1);
    }
}