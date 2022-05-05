package brum.persistence.impl;

import brum.model.dto.common.PaginatedResponse;
import brum.model.dto.identities.*;
import brum.persistence.IdentityPersistenceGateway;
import brum.persistence.entity.IdentityEntity;
import brum.persistence.filters.mapper.PaginationMapper;
import brum.persistence.filters.mapper.IdentitySpecificationMapper;
import brum.persistence.filters.mapper.SortIdentitiesByMapper;
import brum.persistence.filters.specification.IdentitySpecification;
import brum.persistence.mapper.ConstraintExceptionMapper;
import brum.persistence.mapper.IdentityMapper;
import brum.persistence.repository.IdentityRepository;
import brum.persistence.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class IdentityPersistenceGatewayImpl implements IdentityPersistenceGateway {

    private final IdentityRepository identityRepository;
    private final UserRepository userRepository;

    public IdentityPersistenceGatewayImpl(IdentityRepository identityRepository,
                                          UserRepository userRepository) {
        this.identityRepository = identityRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Identity> getAllIdentities() {
        return getFilteredIdentities(null).getRows();
    }

    @Override
    public PaginatedResponse<Identity> getFilteredIdentities(IdentitySearchCriteria searchCriteria) {
        Specification<IdentityEntity> specifications = combineSpecifications(
                IdentitySpecificationMapper.mapToSpecification(searchCriteria == null ? null : searchCriteria.getFilters()));

        PageRequest pagination = PaginationMapper.map(searchCriteria == null ? null : searchCriteria.getPagination());
        SortIdentities sort = searchCriteria == null ? null : searchCriteria.getSort();
        pagination = pagination.withSort(SortIdentitiesByMapper.INSTANCE.map(
                sort == null ? null : sort.getSortBy(), sort == null ? null : sort.getOrder()));

        Page<IdentityEntity> page = identityRepository.findAll(specifications, pagination);
        PaginatedResponse<Identity> result = new PaginatedResponse<>();
        result.setRows(IdentityMapper.INSTANCE.mapToListFromEntities(page.getContent()));
        result.setCount(page.getTotalElements());
        return result;
    }

    @Override
    public Identity getIdentityByExternalId(String externalId) {
        return IdentityMapper.INSTANCE.mapFromEntity(identityRepository.getIdentityEntityByExternalId(externalId));
    }

    @Override
    public Identity getIdentityByDocumentNumber(String documentNumber) {
        return IdentityMapper.INSTANCE.mapFromEntity(identityRepository.getIdentityEntityByDocumentNumber(documentNumber));
    }

    @Override
    public void saveIdentity(Identity identity) {
        IdentityEntity entity = IdentityMapper.INSTANCE.mapFromDto(identity);
        entity.setCreatedBy(userRepository.getUserEntityByUsername(identity.getCreatedBy().getUsername()));
        try {
            identityRepository.save(entity);
        } catch (DataIntegrityViolationException e) {
            throw ConstraintExceptionMapper.map(e);
        }
    }

    @Override
    public void deleteIdentityById(Long id) {
        identityRepository.deleteById(id);
    }

    private Specification<IdentityEntity> combineSpecifications(List<IdentitySpecification> identitySpecifications) {
        Specification<IdentityEntity> specification =
                (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.conjunction();

        for (IdentitySpecification identitySpecification : identitySpecifications) {
            specification = specification.and(identitySpecification);
        }
        return specification;
    }
}
