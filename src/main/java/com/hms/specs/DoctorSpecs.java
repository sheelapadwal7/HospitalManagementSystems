package com.hms.specs;

import org.springframework.data.jpa.domain.Specification;

import com.hms.model.Doctor;

public class DoctorSpecs {
    
    public static Specification<Doctor> name(String name) {
        return (root, query, builder) -> builder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Doctor> specialization(String specialization) {
        return (root, query, builder) -> builder.like(root.get("specialization"), "%" + specialization + "%");
    }
}

