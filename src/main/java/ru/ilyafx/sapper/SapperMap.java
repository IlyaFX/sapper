package ru.ilyafx.sapper;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("all")
public class SapperMap {

    private int height, width, mines;

    @Getter
    private final Map<Integer, Coordinate[]> coordinates = new HashMap<>();

    private static final Random rand = new Random();

    public void build(int xStart, int yStart) {
        for(int y = 0; y<height; y++) {
            Coordinate[] mas = new Coordinate[width];
            for(int x = 0; x<width; x++) {
                mas[x] = new Coordinate(x, y, false, false, 0, false);
            }
            coordinates.put(y, mas);
        }
        int n = 0;
        while(n != mines) {
            Coordinate cord = get(rand.nextInt(width), rand.nextInt(height));
            if(cord.distance(xStart, yStart) <= 2) continue;
            if(cord.isMine()) continue;
            cord.setMine(true);
            n++;
        }

        for(int y = 0; y<height; y++) {
            for(int x = 0; x<width; x++) {
                Coordinate cord = get(x,y);
                if(cord.isMine()) continue;
                List<Coordinate> list = near(x,y);
                cord.setSize((int) list.stream().filter(Coordinate::isMine).count());
            }
        }

    }

    public List<Coordinate> near(int x, int y){
        List<Coordinate> list = new ArrayList<>(Arrays.asList(
                get(x+1,y),get(x-1,y),
                get(x,y+1),get(x,y-1),
                get(x+1,y+1),get(x+1,y-1),
                get(x-1,y+1),get(x-1,y-1)
        ));
        list.removeIf(cord -> cord==null);
        return list;
    }

    public Coordinate get(int x, int y) {
        try {
            return coordinates.get(y)[x];
        }catch(Exception e){
            return null;
        }
    }

    public boolean flag(int xCord, int yCord) {
        Coordinate cord = get(xCord,yCord);
        if(cord.isFlag()) {
            cord.setFlag(false);
            return false;
        }
        cord.setFlag(true);
        int m = 0;
        int f = 0;
        for(int y = 0; y<height; y++) {
            for(int x = 0; x<width; x++) {
                Coordinate coord = get(x,y);
                if(coord.isMine() && coord.isFlag()) m++;
                if(coord.isFlag()) f++;
            }
        }
        if(m == mines && f == mines) {
            return true;
        }
        return false;
    }

    public boolean open(int x, int y) {
        Coordinate cord = get(x,y);
        if(cord.isOpen()) return true;
        cord.setOpen(true);
        if(cord.isMine()) {
            return false;
        }
        if(cord.getSize() == 0) {
            List<Coordinate> near = near(x,y);
            near.stream().filter(corde -> !corde.isMine()).forEach(corde -> open(corde.getX(), corde.getY()));
        }
        return true;
    }



    @AllArgsConstructor @Getter @Setter
    public static class Coordinate {

        private int x;
        private int y;
        private boolean mine;
        private boolean open;
        private int size;
        private boolean flag;

        public int distance(int xt, int yt) {
            int xf = x-xt;
            if(xf < 0) xf=xf*-1;
            int yf = y-yt;
            if(yf < 0) yf = yf*-1;
            return xf+yf;
        }

    }

    public static SapperMap create(int height, int width, int mines) {
        SapperMap map = new SapperMap(height, width, mines);
        return map;
    }

}
