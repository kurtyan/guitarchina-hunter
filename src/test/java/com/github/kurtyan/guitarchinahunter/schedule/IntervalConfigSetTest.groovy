package com.github.kurtyan.guitarchinahunter.schedule;

import org.codehaus.groovy.runtime.metaclass.ConcurrentReaderHashMap;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by yanke on 2016/4/20.
 */
public class IntervalConfigSetTest {

    @Test
    public void parse() throws Exception {
        IntervalConfigSet set = new IntervalConfigSet()

        LocalTime time = LocalTime.of(0, 0, 0);

        [
                "0000" : time,
                "000000" : time,
                "00:00" : time,
                "00:00:00" : time,
        ].each { key, value ->
            def parsed = set.parse(key)
            assertTrue(parsed.equals(value))
        }
    }

}