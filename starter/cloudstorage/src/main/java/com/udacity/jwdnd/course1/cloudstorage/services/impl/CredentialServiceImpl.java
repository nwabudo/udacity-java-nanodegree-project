package com.udacity.jwdnd.course1.cloudstorage.services.impl;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import com.udacity.jwdnd.course1.cloudstorage.dto.CredForm;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class CredentialServiceImpl implements CredentialService {

    private EncryptionService encryptionService;
    private CredentialMapper credentialMapper;

    public CredentialServiceImpl(EncryptionService encryptionService, CredentialMapper credentialMapper) {
        this.encryptionService = encryptionService;
        this.credentialMapper = credentialMapper;
    }

    private static final String symbols = "ABCDEFGHIJKLMNPRSTUVWXYZ0123456789";

    private final Random random = new SecureRandom();

    private String nextString(int length) {
        if (length < 1)
            throw new IllegalArgumentException("length < 1: " + length);
        final char[] buf = new char[length];
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols.charAt(random.nextInt(symbols.length()));

        return new String(buf);
    }

    private String nextString(){
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        return encodedKey;
    }

    @Override
    public Credential getCredential(int credentialId) {
        return this.credentialMapper.getCredential(credentialId);
    }

    @Override
    public List<Credential> getCredentialsByUserId(int userId) {
        return this.credentialMapper.getCredentials(userId);
    }

    @Override
    public void removeCredential(int credentialId) {
        this.credentialMapper.deleteCredential(credentialId);
    }

    @Override
    public int saveCredential(CredForm credential, int userId) {
        Credential cred = new Credential();
        String key = nextString();
        String encryptedPass = this.encryptionService.encryptValue(credential.getPassword(), key);

        cred.setKey(key);
        cred.setUrl(credential.getUrl());
        cred.setPassword(encryptedPass);
        cred.setUsername(credential.getUsername());
        cred.setUserId(userId);

        return this.credentialMapper.insert(cred);
    }

    @Override
    public int updateCredential(CredForm cred, int userId) {
        Credential credential = this.credentialMapper.getCredential(cred.getCredentialId());

        String key = credential.getKey();
        String encryptedPass = this.encryptionService.encryptValue(cred.getPassword(), key);

        credential.setUrl(cred.getUrl());
        credential.setPassword(encryptedPass);
        credential.setUsername(cred.getUsername());

        return this.credentialMapper.updateCredential(credential);
    }

    @Override
    public List<CredForm> mapTo(List<Credential> credentials){
        List<CredForm> creds = credentials.stream()
               .map(n -> mapTo(n))
               .collect(Collectors.toList());
        return creds;
    }

    @Override
    public void deleteAll() {
        this.credentialMapper.deleteAll();
    }

    public CredForm mapTo(Credential cred) {
        String decryptedPass = this.encryptionService.decryptValue(cred.getPassword(), cred.getKey());
        return new CredForm(cred.getCredentialId(), cred.getUrl(), cred.getUsername(), decryptedPass, cred.getPassword());
    }
}
