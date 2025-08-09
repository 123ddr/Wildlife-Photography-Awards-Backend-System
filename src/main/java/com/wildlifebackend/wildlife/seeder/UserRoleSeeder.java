package com.wildlifebackend.wildlife.seeder;



import com.wildlifebackend.wildlife.entitiy.UserRoleEntity;
import com.wildlifebackend.wildlife.enums.UserRole;
import com.wildlifebackend.wildlife.repository.UserRoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserRoleSeeder {

    @Autowired
    private UserRoleRepo repository;

    public void seed() {
        for (UserRole type : UserRole.values()) {
            repository.findById(type.getKey())
                    .ifPresentOrElse(existing -> {
                                // Update the name if it's different
                                if (!existing.getRoleName().equals(type.getValue())) {
                                    existing.setRoleName(type.getValue());
                                    repository.save(existing);
                                }
                            },
                            // Insert if not exists
                            () -> {
                                UserRoleEntity newType = new UserRoleEntity();
                                // Don't set ID manually when using IDENTITY generation
                                newType.setRoleName(type.getValue());
                                repository.save(newType);
                            });
        }
    }
}
