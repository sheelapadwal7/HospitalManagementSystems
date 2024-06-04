package com.hms.specs;

import org.springframework.data.jpa.domain.Specification;
import com.hms.model.Patient;

public class PatientSpecs {

    public static Specification<Patient> name(String name) {
        return (root, query, builder) -> builder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Patient> age(Integer age) {
        return (root, query, builder) -> builder.equal(root.get("age"), age);
    }

    public static Specification<Patient> gender(String gender) {
        return (root, query, builder) -> builder.equal(root.get("gender"), gender);
    }

    public static Specification<Patient> address(String address) {
        return (root, query, builder) -> builder.like(root.get("address"), "%" + address + "%");
    }
}

