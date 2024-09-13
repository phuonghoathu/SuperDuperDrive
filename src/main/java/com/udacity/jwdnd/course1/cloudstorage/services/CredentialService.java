package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.dto.CredentialDto;
import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CredentialService {
    final CredentialMapper credentialMapper;
    final EncryptionService encryptionService;

    public List<CredentialDto> getAllCredentialByUsername(String username) {
        List<Credential> credentials = credentialMapper.getAllCredentialByUser(username);
        List<CredentialDto> credentialDtos = new ArrayList<CredentialDto>();
        CredentialDto credentialDto ;
        for (Credential credential : credentials) {
            credentialDto = new CredentialDto(credential.getCredentialId(),
                    credential.getUrl(),
                    credential.getUsername(),
                    credential.getKey(),
                    credential.getPassword(),
                    credential.getUserId());
            credentialDto.setPassEncode(encryptionService.decryptValue(credential.getPassword(), credential.getKey()));
            credentialDtos.add(credentialDto);
        }
        return credentialDtos;
    }

    public int addCredential(Credential credential) throws IOException {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        credential.setPassword(encryptedPassword);
        credential.setKey(encodedKey);
        return credentialMapper.insert(credential);
    }

    public int updateCredential(Credential credential) throws IOException {
        // Get old credential
        Credential oldCre = credentialMapper.getCredentialById(credential.getCredentialId());
        if (oldCre == null) {
            return -1;
        }
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), oldCre.getKey());

        credential.setPassword(encryptedPassword);
        credential.setKey(oldCre.getKey());
        return credentialMapper.update(credential);
    }

    public int deleteCredential(int credentialId, int userId) {
        return credentialMapper.delete(credentialId, userId);
    }
}
