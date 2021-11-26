package com.example.news.util

/**
 * Interface for mapping between entity model and domain model
 * @param Entity the entity model type
 * @param DomainModel the domain model type
 */
interface EntityMapper <Entity, DomainModel>{

    /**
     * Maps from entity model to domain model
     * @param entity the entity model
     * @return the domain model
     */
    fun mapFromEntity(entity: Entity): DomainModel

    /**
     * Maps from domain model to entity model
     * @param domainModel the domain model
     * @return the entity model
     */
    fun mapToEntity(domainModel: DomainModel): Entity
}
