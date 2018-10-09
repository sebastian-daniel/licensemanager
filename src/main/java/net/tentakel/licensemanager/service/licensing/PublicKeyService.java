/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tentakel.licensemanager.service.licensing;

import java.io.File;
import net.tentakel.licensemanager.service.licensing.exception.LicenseException;

/**
 *
 * @author sebastian-daniel.herzog
 */
public interface PublicKeyService 
{
    public boolean checkPublicKeySha512Hash(File publicKeyFile, String publicKeyHash) throws LicenseException;
}
