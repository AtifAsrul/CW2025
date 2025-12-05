package com.comp2042.logic.bricks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomBrickGenerator implements BrickGenerator {

    private final List<Brick> brickList;

    private final Deque<Brick> nextBricks = new ArrayDeque<>();

    public RandomBrickGenerator() {
        brickList = new ArrayList<>();
        brickList.add(new IBrick());
        brickList.add(new JBrick());
        brickList.add(new LBrick());
        brickList.add(new OBrick());
        brickList.add(new SBrick());
        brickList.add(new TBrick());
        brickList.add(new ZBrick());

        refillBag();
        refillBag(); // Fill buffer
    }

    private void refillBag() {
        List<Brick> bag = new ArrayList<>(brickList);
        // Shuffle the bag
        for (int i = 0; i < bag.size(); i++) {
            int index = ThreadLocalRandom.current().nextInt(bag.size());
            Brick temp = bag.get(i);
            bag.set(i, bag.get(index));
            bag.set(index, temp);
        }
        nextBricks.addAll(bag);
    }

    @Override
    public Brick getBrick() {
        if (nextBricks.size() <= 7) {
            refillBag();
        }
        return nextBricks.poll();
    }

    @Override
    public Brick getNextBrick() {
        return nextBricks.peek();
    }
}
