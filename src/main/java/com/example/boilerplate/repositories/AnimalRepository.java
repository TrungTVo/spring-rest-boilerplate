package com.example.boilerplate.repositories;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.boilerplate.commons.models.Animal;

public interface AnimalRepository extends JpaRepository<Animal, UUID> {

	@Query(value = """
			select *
			from animal
			where (created_at, id) > (:createdAt, :id)
			order by created_at asc, id asc
			""", nativeQuery = true)
	List<Animal> findNextByCreatedAtAsc(
			@Param("createdAt") Instant createdAt,
			@Param("id") UUID id,
			Pageable pageable);

	@Query(value = """
			select *
			from animal
			where (created_at, id) < (:createdAt, :id)
			order by created_at desc, id desc
			""", nativeQuery = true)
	List<Animal> findNextByCreatedAtDesc(
			@Param("createdAt") Instant createdAt,
			@Param("id") UUID id,
			Pageable pageable);

	@Query(value = """
			select *
			from animal
			where (name, created_at, id) > (:name, :createdAt, :id)
			order by name asc, created_at asc, id asc
			""", nativeQuery = true)
	List<Animal> findNextByNameAsc(
			@Param("name") String name,
			@Param("createdAt") Instant createdAt,
			@Param("id") UUID id,
			Pageable pageable);

	@Query(value = """
			select *
			from animal
			where (name, created_at, id) < (:name, :createdAt, :id)
			order by name desc, created_at desc, id desc
			""", nativeQuery = true)
	List<Animal> findNextByNameDesc(
			@Param("name") String name,
			@Param("createdAt") Instant createdAt,
			@Param("id") UUID id,
			Pageable pageable);

	@Query(value = """
			select *
			from animal
			where (age, created_at, id) > (:age, :createdAt, :id)
			order by age asc, created_at asc, id asc
			""", nativeQuery = true)
	List<Animal> findNextByAgeAsc(
			@Param("age") Integer age,
			@Param("createdAt") Instant createdAt,
			@Param("id") UUID id,
			Pageable pageable);

	@Query(value = """
			select *
			from animal
			where (age, created_at, id) < (:age, :createdAt, :id)
			order by age desc, created_at desc, id desc
			""", nativeQuery = true)
	List<Animal> findNextByAgeDesc(
			@Param("age") Integer age,
			@Param("createdAt") Instant createdAt,
			@Param("id") UUID id,
			Pageable pageable);
}
