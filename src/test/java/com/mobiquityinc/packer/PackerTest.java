package com.mobiquityinc.packer;

import static org.junit.Assert.assertEquals;

import com.mobiquityinc.exception.APIException;
import org.junit.Test;

public class PackerTest {

  @Test
  public void packTest1() throws APIException {
    String result = Packer.pack("testFiles/test1");
    assertEquals("4\n", result);
  }

  @Test
  public void packTest2() throws APIException {
    String result = Packer.pack("testFiles/test2");
    assertEquals("-\n", result);
  }

  @Test
  public void packTest3() throws APIException {
    String result = Packer.pack("testFiles/test3");
    assertEquals("2,7\n", result);
  }

  @Test
  public void packTest4() throws APIException {
    String result = Packer.pack("testFiles/test4");
    assertEquals("8,9\n", result);
  }
  @Test
  public void packTest5() throws APIException {
    String result = Packer.pack("testFiles/test5");
    assertEquals("1,3\n", result);
  }

}
