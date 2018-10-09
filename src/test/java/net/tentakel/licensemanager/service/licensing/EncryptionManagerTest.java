/**
 * Copyright(C) 2013 Patrik Dufresne Service Logiciel <info@patrikdufresne.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.tentakel.licensemanager.service.licensing;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import org.testng.Assert;

/**
 * Test the functionality provided by the class {@link EncryptionManager}
 * 
 * @author Patrik Dufresne
 * 
 */
public class EncryptionManagerTest 
{

    private EncryptionManager manager;
    private byte[] data;

    // @BeforeTest
    public void initEncryptionManager() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException 
    {
        byte[] pubKey = EncryptionManager.readAll(getClass().getResourceAsStream("/my_pub_key.der"));
        byte[] privateKey = EncryptionManager.readAll(getClass().getResourceAsStream("/my_priv_key.der"));
        this.manager = new EncryptionManager(pubKey, privateKey);

        // Prepare data
        String string = "This is some data to by sign";
        this.data = string.getBytes();
    }

    // @Test
    public void sign_WithData_ReturnSignature() throws InvalidKeyException, NoSuchAlgorithmException, SignatureException 
    {
        byte[] sig = this.manager.sign(data);
    }

    // @Test
    public void verify_WithDataAndGoodSignature_ReturnTrue() throws InvalidKeyException,
            NoSuchAlgorithmException, SignatureException, FileNotFoundException, IOException 
    {
        byte[] sig = this.manager.sign(data);
        
        Assert.assertTrue(this.manager.verify(data, sig));
    }

    // @Test
    public void verify_WithDataAndWrongSignature_ReturnTrue() throws InvalidKeyException,
            NoSuchAlgorithmException, SignatureException, FileNotFoundException, IOException 
    {
        byte[] sig = this.manager.sign(data);
        sig[0] = (byte) (((int) sig[0]) + 3);

        Assert.assertFalse(this.manager.verify(data, sig));
    }

}
