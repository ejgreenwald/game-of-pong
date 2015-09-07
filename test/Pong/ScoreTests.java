package Pong;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pong.Score;
import org.junit.Assert;

/**
 *
 * @author lejtman
 */
public class ScoreTests {
    
    public ScoreTests() {
    }
    
    Score s1,s2a,s2b;
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        s1= new Score("a",1);
        s2a= new Score("b",2);
        s2b= new Score("c",2);
                
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void scoreCompareTests(){
        Assert.assertTrue(s1.compareTo(s2a) < 0);
        Assert.assertTrue(s2b.compareTo(s2a) == 0);
        Assert.assertTrue(s2a.compareTo(s1) > 0);
        System.out.println(s1.toString());
    }
}
