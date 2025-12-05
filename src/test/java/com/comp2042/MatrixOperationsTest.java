package com.comp2042;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MatrixOperationsTest {

    @Test
    void testBoundaryCollision() {
        // Create a 10x20 grid (standard Tetris size)
        int[][] matrix = new int[20][10];

        // Define a simple 2x2 block (O-shape)
        int[][] brick = {
                { 1, 1 },
                { 1, 1 }
        };

        // Test left boundary collision (x = -1)
        assertTrue(MatrixOperations.intersect(matrix, brick, -1, 0), "Should collide when moving past left boundary");

        // Test right boundary collision (x = 9 for a width 2 block means it occupies 9
        // and 10, 10 is out of bounds)
        // Board width is 10 (indices 0-9). If x=9, block occupies x=9 and x=10. x=10 is
        // out of bound.
        assertTrue(MatrixOperations.intersect(matrix, brick, 9, 0), "Should collide when moving past right boundary");

        // Test bottom boundary collision
        // Board height is 20 (indices 0-19). If y=19, block occupies y=19 and y=20.
        // y=20 is out of bound.
        assertTrue(MatrixOperations.intersect(matrix, brick, 0, 19), "Should collide when moving past bottom boundary");

        // Test inside safe space
        assertFalse(MatrixOperations.intersect(matrix, brick, 0, 0), "Should not collide when inside the board");
        assertFalse(MatrixOperations.intersect(matrix, brick, 4, 10), "Should not collide when inside the board");
    }

    @Test
    void testOverlapCollision() {
        int[][] matrix = new int[20][10];

        // Place a block at (5, 5)
        matrix[5][5] = 1;

        // Define a 1x1 brick
        int[][] brick = { { 1 } };

        // Try to move into the occupied cell
        assertTrue(MatrixOperations.intersect(matrix, brick, 5, 5), "Should collide with existing block");

        // Try to move into an empty cell
        assertFalse(MatrixOperations.intersect(matrix, brick, 5, 4), "Should not collide with empty cell");
    }

    @Test
    void testLineClearing() {
        int[][] matrix = new int[20][10];

        // Fill row 19 (bottom row) with blocks
        for (int i = 0; i < 10; i++) {
            matrix[19][i] = 1;
        }

        // Fill row 18 with blocks as well
        for (int i = 0; i < 10; i++) {
            matrix[18][i] = 1;
        }

        // Fill row 17 partially
        matrix[17][0] = 1;

        ClearRow result = MatrixOperations.checkRemoving(matrix);

        // Expect 2 lines cleared
        assertEquals(2, result.getLinesRemoved(), "Should clear 2 full rows");

        // Verify the new matrix state
        int[][] newMatrix = result.getNewMatrix();

        // The partially filled row (originally 17) should shift down by 2 to become row
        // 19
        assertEquals(1, newMatrix[19][0], "Partially filled row should shift down");
        assertEquals(0, newMatrix[19][1], "Empty part of shifted row should remain empty");

        // The rows above should be empty
        for (int i = 0; i < 10; i++) {
            assertEquals(0, newMatrix[18][i], "Row above shifted content should be empty");
        }
    }
}
