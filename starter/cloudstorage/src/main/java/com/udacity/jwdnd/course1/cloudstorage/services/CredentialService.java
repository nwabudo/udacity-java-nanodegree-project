package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import com.udacity.jwdnd.course1.cloudstorage.dto.CredForm;

import java.util.List;

public interface CredentialService {

    Credential getCredential(int credentialId);

    CredForm mapTo(Credential cred);

    List<Credential> getCredentialsByUserId(int userId);

    void removeCredential(int credentialId);

    int saveCredential(CredForm credential, int userId);

    int updateCredential(CredForm cred, int userId);

    List<CredForm> mapTo(List<Credential> credentials);

    void deleteAll();
}
