package com.pm.journalapp.service;

import com.pm.journalapp.entity.JournalEntry;
import com.pm.journalapp.entity.User;
import com.pm.journalapp.repository.JournalEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class JournalEntryService {

    private final JournalEntryRepository journalEntryRepository;
    private final UserService userService;

    @Transactional
    public void saveEntry (JournalEntry journalEntry, String username) {
        try {
            User user = userService.findByUsername(username);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
            userService.saveUser(user);
        } catch (Exception e) {
            log.error("Error while saving journal entry for user: {}", username, e);
            throw new RuntimeException("An error occurred while saving the journal entry");
        }
    }

    public void saveEntry (JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    @Transactional
    public boolean deleteById(ObjectId id, String username) {
        boolean removed = false;
        try {
            User  user = userService.findByUsername(username);
            removed = user.getJournalEntries().removeIf(j -> j.getId().equals(id));
            if(removed){
                userService.saveUser(user);
                journalEntryRepository.deleteById(id);
            }
        }catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException("An error occurred while deleting the journal entry {}", e);
        }
        return removed;
    }

}
