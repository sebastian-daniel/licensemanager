/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tentakel.licensemanager.views;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.tentakel.licensemanager.domain.License;
import net.tentakel.licensemanager.service.licensing.LicenseManager;
import net.tentakel.licensemanager.service.licensing.LicenseService;
import net.tentakel.licensemanager.service.licensing.implementation.LicenseManagerImpl;
import net.tentakel.licensemanager.service.webconfiguration.ApplicationConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author kirk3
 */
@Controller
@RequestMapping("/view/license")
public class LicenseController {

 
    
    @Autowired
    private ApplicationConfigurationService applicationConfigurationService;
    
    
    private ArrayList<String> getLicenseType()
    {
        ArrayList<String> licenseType = new ArrayList<String>();
            licenseType.add("lifetime");
            licenseType.add("single-server");
            licenseType.add("trial");
		
        return licenseType;
    }
    
    
    
    @RequestMapping(path="/create-license", method = RequestMethod.GET)
    public ModelAndView createLicense() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("CreateLicense");
        modelAndView.addObject("license", new License());
        
        ArrayList<String> licenseType = getLicenseType();
        modelAndView.addObject("allRoles", licenseType);
        
        return modelAndView;
   }
    @RequestMapping(path="/create-license", method = RequestMethod.POST)
    public String createLicense(License license, BindingResult result) 
    {
       //licTypen: lifetime, single-server, trial
       // -> Anpassung in html, methode im Controller und Anpassung in Domain Klasse
       ModelAndView modelAndView = new ModelAndView();
       
       Date date = java.util.Calendar.getInstance().getTime();
       SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
       String dateString = dateFormatter.format(date);
       
       String licenseFolderName = applicationConfigurationService.getLicenseFolderName();
       String licenseFolder = LicenseController.class.getClassLoader().getResource("").getPath() + licenseFolderName + File.separator ;
       
       String fileName = dateString + "_" + license.getFirstName()+ ".lic";

       File outputFolder = new File(licenseFolder);
              
       String privateKeyName = applicationConfigurationService.getPrivateKeyName();
       String publicKeyName = applicationConfigurationService.getPublicKeyName();
             
       File publicKeyFile = new File(licenseFolder + File.separator + publicKeyName);
       File privateKeyFile = new File(licenseFolder + File.separator + privateKeyName);
       
        try {
            LicenseManager licenseManager = new LicenseManagerImpl(publicKeyFile, privateKeyFile);
            LicenseService licenseService = new LicenseService();
            licenseService.setProperty(LicenseService.ID, "001");
            licenseService.setProperty(LicenseService.NAME, license.getFirstName());
            licenseService.setExpiration(license.getLicenseExpiration());
            licenseService.setProperty(LicenseService.EMAIL, license.getEmail());
            licenseService.setProperty(LicenseService.LICENSE_NUMBER, license.getLicenseNumber());
            licenseService.setProperty(LicenseService.LICENSE_TYPE, license.getLicenseType());
            licenseService.setProperty(LicenseService.VERSION, license.getLicenseVersion());
            outputFolder = new File(licenseFolder+fileName);
            licenseManager.writeLicense(licenseService, outputFolder);
            
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(LicenseController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LicenseController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       
       
       
        return "Erfolg!";
   
    }
}
