package com.test.blog;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IteratorTest {
    @Test
    public void test01(){
        List<Integer> list = new ArrayList<>();
        list.add(12);
        list.add(24);
        list.add(54);
        list.add(543);
        list.add(12);
        Iterator<Integer> iterator = list.iterator();
        Integer t;
        while (iterator.hasNext()){
            t =iterator.next();
            if (t ==12){
                iterator.remove();
            }
        }
        System.out.println(list);
    }
}
