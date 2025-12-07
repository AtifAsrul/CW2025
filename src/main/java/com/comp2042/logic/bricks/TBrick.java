package com.comp2042.logic.bricks;

import com.comp2042.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

final class TBrick implements Brick {

        private final List<int[][]> brickMatrix = new ArrayList<>();

        /**
         * Constructs a new TBrick.
         * Defines the shape matrices for its rotations.
         */
        public TBrick() {
                brickMatrix.add(new int[][] {
                                { 0, 0, 0, 0 },
                                { 6, 6, 6, 0 },
                                { 0, 6, 0, 0 },
                                { 0, 0, 0, 0 }
                });
                brickMatrix.add(new int[][] {
                                { 0, 6, 0, 0 },
                                { 0, 6, 6, 0 },
                                { 0, 6, 0, 0 },
                                { 0, 0, 0, 0 }
                });
                brickMatrix.add(new int[][] {
                                { 0, 6, 0, 0 },
                                { 6, 6, 6, 0 },
                                { 0, 0, 0, 0 },
                                { 0, 0, 0, 0 }
                });
                brickMatrix.add(new int[][] {
                                { 0, 6, 0, 0 },
                                { 6, 6, 0, 0 },
                                { 0, 6, 0, 0 },
                                { 0, 0, 0, 0 }
                });
        }

        /**
         * Gets the shape matrix for this brick.
         *
         * @return list of rotation matrices.
         */
        @Override
        public List<int[][]> getShapeMatrix() {
                return MatrixOperations.deepCopyList(brickMatrix);
        }
}
