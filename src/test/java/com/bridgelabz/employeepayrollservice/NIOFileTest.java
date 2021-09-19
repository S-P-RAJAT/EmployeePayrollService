package com.bridgelabz.employeepayrollservice;


import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.IntStream;

public class NIOFileTest {

    private static final String HOME = System.getProperty("user.home");
    private static final String PLAY_WITH_NIO = "TempPlayGround";

    @Test
    public void givenPathWhenCheckedThenConfirm() {

        Path homePath = Paths.get(HOME);
        Assert.assertTrue(Files.exists(homePath));

    }
}