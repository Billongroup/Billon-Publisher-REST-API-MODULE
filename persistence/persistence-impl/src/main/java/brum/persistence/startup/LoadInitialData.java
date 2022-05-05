package brum.persistence.startup;

import brum.common.utils.ExternalId;
import brum.common.enums.security.PrivilegeEnum;
import brum.common.enums.security.RoleEnum;
import brum.common.enums.security.SecurityDocumentation;
import brum.common.logger.BrumLogger;
import brum.common.logger.factory.BrumLoggerFactory;
import brum.persistence.entity.PrivilegeEntity;
import brum.persistence.entity.RoleEntity;
import brum.persistence.repository.PrivilegeRepository;
import brum.persistence.repository.RoleRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;

@Component
public class LoadInitialData implements ApplicationListener<ContextRefreshedEvent> {

    private final BrumLogger log = BrumLoggerFactory.create(this.getClass());

    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;

    public LoadInitialData(RoleRepository roleRepository, PrivilegeRepository privilegeRepository) {
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
    }

    private final Map<RoleEnum, RoleEntity> roleMap = new EnumMap<>(RoleEnum.class);

    private boolean initialized = false;

    @Override
    @Transactional
    public void onApplicationEvent(@Nullable ContextRefreshedEvent event) {
        if (initialized) {
            return;
        }
        initializeRoles();
        initializePrivileges();
        roleRepository.saveAll(roleMap.values());
        initialized = true;
    }

    private void initializeRoles() {
        List<RoleEntity> roleEntities = roleRepository.findAll();
        log.info("Roles existing in db: {}", roleEntities);
        for (RoleEnum role : RoleEnum.values()) {
            if (roleExists(roleEntities, role)) {
                roleMap.put(role, findRole(roleEntities, role));
            } else {
                RoleEntity roleEntity = new RoleEntity();
                roleEntity.setName(role);
                roleEntity.setExternalId(ExternalId.ROLE.generateId());
                log.info("Creating new role: {}", roleEntity);
                roleMap.put(role, roleEntity);
            }
        }
    }

    private void initializePrivileges() {
        List<PrivilegeEntity> privilegeEntities = privilegeRepository.findAll();
        for (SecurityDocumentation.Entry entry : SecurityDocumentation.getDocumentation()) {
            if (entry.getRoles().length == 0) {
                continue;
            }
            if (privilegeExists(privilegeEntities, entry.getPrivilege())) {
                PrivilegeEntity privilegeEntity = findPrivilege(privilegeEntities, entry.getPrivilege());
                for (RoleEnum role : entry.getRoles()) {
                    RoleEntity roleEntity = roleMap.get(role);
                    roleEntity.getRolePrivilege().add(privilegeEntity);
                }
            } else {
                PrivilegeEntity privilegeEntity = new PrivilegeEntity();
                privilegeEntity.setExternalId(ExternalId.PRIVILEGE.generateId());
                privilegeEntity.setName(entry.getPrivilege());
                privilegeEntity.setDescription(entry.getDescription());
                for (RoleEnum role : entry.getRoles()) {
                    RoleEntity roleEntity = roleMap.get(role);
                    privilegeEntity.getPrivilegeRole().add(roleEntity);
                    roleEntity.getRolePrivilege().add(privilegeEntity);
                }
                privilegeEntities.add(privilegeEntity);
                log.info("Creating new privilege: ", privilegeEntity);
            }
        }
        privilegeRepository.saveAll(privilegeEntities);
    }

    private boolean roleExists(List<RoleEntity> roleEntities, RoleEnum role) {
        for (RoleEntity entity : roleEntities) {
            if (entity.getName().equals(role)) {
                return true;
            }
        }
        return false;
    }

    private RoleEntity findRole(List<RoleEntity> roleEntities, RoleEnum role) {
        for (RoleEntity entity : roleEntities) {
            if (entity.getName().equals(role)) {
                return entity;
            }
        }
        return null;
    }

    private boolean privilegeExists(List<PrivilegeEntity> privilegeEntities, PrivilegeEnum privilege) {
        for (PrivilegeEntity privilegeEntity : privilegeEntities) {
            if (privilegeEntity.getName().equals(privilege)) {
                return true;
            }
        }
        return false;
    }

    private PrivilegeEntity findPrivilege(List<PrivilegeEntity> privilegeEntities, PrivilegeEnum privilege) {
        for (PrivilegeEntity entity : privilegeEntities) {
            if (entity.getName().equals(privilege)) {
                return entity;
            }
        }
        return null;
    }
}
