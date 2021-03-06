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

import net.tentakel.licensemanager.service.licensing.KeyManager;
import net.tentakel.licensemanager.service.licensing.exception.KeyBlackListedException;
import net.tentakel.licensemanager.service.licensing.exception.KeyInvalidException;

public class KeyManagerTest 
{
    private KeyManager manager;

    // @BeforeTest
    public void initKeyManager() 
    {
        this.manager = new KeyManager();
    }

    // @Test
    public void generateKey_WithAuthCode_ReturnKey() 
    {
        String key = this.manager.generateKey(3, "coucou");
    }

    // @Test
    public void validateKey_WithValidKey_ReturnTrue() throws KeyInvalidException, KeyBlackListedException 
    {
        String key = this.manager.generateKey(3, "coucou");

        this.manager.validateKey(key);
    }

    // @Test(expectedExceptions = KeyInvalidException.class)
    public void validateKey_WithInvalidKey_ReturnTrue() throws KeyInvalidException, KeyBlackListedException 
    {
        String key = this.manager.generateKey(3, "coucou");

        key = key.replace("0", "1");

        this.manager.validateKey(key);
    }
}
