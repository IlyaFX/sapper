package ru.ilyafx.sapper;

import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SapperTest {

    public static void main(String[] args) {
        SapperMap map = SapperMap.create(10, 10, 30);
        print(map);
        Scanner scan = new Scanner(System.in);
        while(true) {
            try {
                String deal = scan.next().toLowerCase();
                int x = scan.nextInt() - 1, y = scan.nextInt() - 1;
                if (deal.equals("flag")) {
                    if(map.getCoordinates().isEmpty()) return;
                    if (map.flag(x, y)) {
                        print(map);
                        System.out.println("GAME ENDED! YOU WIN!");
                        System.exit(0);
                        return;
                    }
                } else if (deal.equals("open")) {
                    if(map.getCoordinates().isEmpty()) map.build(x, y);
                    if (!map.open(x, y)) {
                        print(map);
                        System.out.println("GAME ENDED! YOU LOSE");
                        System.exit(0);
                        return;
                    }
                }
                print(map);
            }catch(Exception ignored) {
            }
        }
    }

    private static void print(SapperMap map) {
        Map<Integer, SapperMap.Coordinate[]> m = map.getCoordinates();
        for(Integer i : m.keySet()) {
            SapperMap.Coordinate[] coords = m.get(i);
            String sir = Stream.of(coords).map(cord -> cord.isOpen() ? (cord.isMine() ? "*" : cord.getSize() == 0 ? "_" : "" + cord.getSize()) : cord.isFlag() ? "&" : "X").collect(Collectors.joining(""));
            System.out.println(sir);
        }
    }

}
