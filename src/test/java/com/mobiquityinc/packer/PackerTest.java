package com.mobiquityinc.packer;

import static org.junit.Assert.assertEquals;

import com.mobiquityinc.exception.APIException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PackerTest {

  @Rule
  public ExpectedException expectedEx = ExpectedException.none();

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

  @Test
  public void packTest_FileDoNotExist() throws Exception {
    expectedEx.expect(APIException.class);
    expectedEx.expectMessage("File testFiles/testX does not exist!");
    Packer.pack("testFiles/testX");
  }

  @Test
  public void packTest6() throws APIException {
    expectedEx.expect(APIException.class);
    expectedEx.expectMessage("Package max weight exceeds the maximum weight allowed: \n"
        + "101 : (1,5.1,€10) (2,5.2,€10) (3,6.1,€15)");
    Packer.pack("testFiles/test6");
  }

  @Test
  public void packTest7() throws Exception {
    expectedEx.expect(APIException.class);
    expectedEx.expectMessage("Line items exceeds maximum allowed: \n"
        + "(1,5.1,€10) (2,5.2,€10) (3,6.1,€15) (4,6.1,€15) (5,6.1,€15) (6,6.1,€15) (7,6.1,€15) "
        + "(8,6.1,€15) (9,6.1,€15) (10,6.1,€15) (11,6.1,€15) (12,6.1,€15) (13,6.1,€15) (14,6.1,"
        + "€15) (15,6.1,€15) (16,6.1,€15");
    Packer.pack("testFiles/test7");
  }

  @Test
  public void packTest8() throws APIException {
    expectedEx.expect(APIException.class);
    expectedEx.expectMessage("Item weight or cost exceeds maximum allowed: \n"
        + "(1,5.1,€101)");
    Packer.pack("testFiles/test8");
  }

  @Test
  public void packTest9() throws APIException {
    expectedEx.expect(APIException.class);
    expectedEx.expectMessage("Item weight or cost exceeds maximum allowed: \n"
        + "(1,5.1,€101)");
    Packer.pack("testFiles/test8");
  }
}
