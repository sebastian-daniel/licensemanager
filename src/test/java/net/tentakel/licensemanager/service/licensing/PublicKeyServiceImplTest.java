/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tentakel.licensemanager.service.licensing;

import com.google.common.io.Files;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import net.tentakel.licensemanager.service.webconfiguration.ApplicationConfigurationService;
import net.tentakel.licensemanager.service.webconfiguration.implementation.ApplicationConfigurationServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 *
 * @author sebastian-daniel.herzog
 */
public class PublicKeyServiceImplTest 
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PublicKeyServiceImplTest.class);
    
    private ApplicationConfigurationService applicationConfigurationService;
    
    private String licenseFolderName;
    private String publicKeyName;
    private String publicKeyHash;
    private String licenseFolder;
    private static final String PUBKEY_WRONG_MD5HASH = "d41d8cd98f00b204e9800998ecf8427e";
    
    @BeforeTest
    public void initPublicKeyTest() throws GeneralSecurityException, IOException 
    {
        applicationConfigurationService = new ApplicationConfigurationServiceImpl();
        licenseFolderName = applicationConfigurationService.getLicenseFolderName();
        publicKeyName = applicationConfigurationService.getPublicKeyName();
        publicKeyHash = applicationConfigurationService.getPublicKeyHash();
        
        licenseFolder = PublicKeyServiceImplTest.class.getClassLoader()
                .getResource("").getPath() + licenseFolderName + File.separator ;
    }
    
    @Test
    public void checkPublicKeyExists() throws FileNotFoundException, IOException 
    {
        File publicKeyFile = new File(licenseFolder + File.separator + publicKeyName);
        
        Assert.assertTrue(publicKeyFile.exists());
    }
    
    @Test (dependsOnMethods = { "checkPublicKeyExists" })
    public void checkPublicKeyCorrectHash() throws FileNotFoundException, IOException 
    {
        File publicKeyFile = new File(licenseFolder + File.separator + publicKeyName);
        
        String hash;
        try (FileInputStream pubKeyInputStream = new FileInputStream(publicKeyFile)) 
        {
            hash = DigestUtils.sha512Hex(pubKeyInputStream);
        }
        
        Assert.assertEquals(hash, publicKeyHash);        
    }
    
    @Test (dependsOnMethods = { "checkPublicKeyExists" })
    public void checkPublicKeyWrongHash() throws FileNotFoundException, IOException 
    {
        File publicKeyFile = new File(licenseFolder + File.separator + publicKeyName);
        
        String hash;
        try (FileInputStream pubKeyInputStream = new FileInputStream(publicKeyFile)) 
        {
            hash = DigestUtils.sha512Hex(pubKeyInputStream);
        }
        
        Assert.assertNotEquals(hash, PUBKEY_WRONG_MD5HASH);        
    }
    
    @Test (dependsOnMethods = { "checkPublicKeyExists" })
    public void checkCorrectHashesOfCopiedFiles() throws FileNotFoundException, IOException 
    {
        File publicKeyFile = new File(licenseFolder + File.separator + publicKeyName);
        
        File tempFile01 = new File(licenseFolder + "testfolder01");
        boolean success = (tempFile01).mkdirs();
        Assert.assertTrue(success);
        
        File tempFile02 = new File(licenseFolder + "testfolder02");
        success = (tempFile02).mkdirs();
        Assert.assertTrue(success);
        
        Files.copy(publicKeyFile, new File(tempFile01 + File.separator + publicKeyName));
        Files.copy(publicKeyFile, new File(tempFile02 + File.separator + publicKeyName));
        
        String originalFileHash;
        try (FileInputStream pubKeyInputStream = new FileInputStream(publicKeyFile)) 
        {
            originalFileHash = DigestUtils.sha512Hex(pubKeyInputStream);
        }
        Assert.assertEquals(originalFileHash, publicKeyHash);
        
        String tempFile01Hash;
        try (FileInputStream tempFile01InputStream = new FileInputStream(tempFile01 + File.separator + publicKeyName)) 
        {
            tempFile01Hash = DigestUtils.sha512Hex(tempFile01InputStream);
        }
        Assert.assertEquals(tempFile01Hash, publicKeyHash);
        
        String tempFile02Hash;
        try (FileInputStream tempFile02InputStream = new FileInputStream(tempFile02 + File.separator + publicKeyName)) 
        {
            tempFile02Hash = DigestUtils.sha512Hex(tempFile02InputStream);
        }
        Assert.assertEquals(tempFile02Hash, publicKeyHash);
        
        FileUtils.deleteDirectory(tempFile01);
        FileUtils.deleteDirectory(tempFile02);
    }
    
    @Test (dependsOnMethods = { "checkPublicKeyExists" })
    public void checkCorrectHashesOfSrcAndTestPubKey() throws FileNotFoundException, IOException 
    {
        File srcPubKeyFile = new File("D:\\Users\\SebastianDanielHerzo\\Documents\\privat\\opstool\\server\\src\\main\\resources\\lic\\my_pub_key.der");
        File testPubKeyFile = new File("D:\\Users\\SebastianDanielHerzo\\Documents\\privat\\opstool\\server\\src\\test\\resources\\lic\\my_pub_key.der");
        
        String srcFileHash;
        try (FileInputStream pubKeyInputStream = new FileInputStream(srcPubKeyFile)) 
        {
                srcFileHash = DigestUtils.sha512Hex(pubKeyInputStream);
        }
        Assert.assertEquals(srcFileHash, publicKeyHash);

        String testFileHash;
         try (FileInputStream testFileInputStream = new FileInputStream(testPubKeyFile))
        {
                testFileHash = DigestUtils.sha512Hex(testFileInputStream);
        }
        Assert.assertEquals(testFileHash, publicKeyHash);        
    }
    
    @Test (dependsOnMethods = { "checkPublicKeyExists" })
    public void checkCorrectHashFromPublicKeyService() throws FileNotFoundException, IOException 
    {
        // TODO: has to be done!
    }
}
