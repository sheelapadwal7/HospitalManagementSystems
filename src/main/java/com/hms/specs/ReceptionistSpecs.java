package com.hms.specs;

import org.springframework.data.jpa.domain.Specification;
import com.hms.model.Receptionist;

public class ReceptionistSpecs {

    public static Specification<Receptionist> name(String name) {
        return (root, query, builder) -> builder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Receptionist> department(String department) {
        return (root, query, builder) -> builder.like(root.get("department"), "%" + department + "%");
    }

    public static Specification<Receptionist> email(String email) {
        return (root, query, builder) -> builder.like(root.get("email"), "%" + email + "%");
    }

    public static Specification<Receptionist> phone(String phone) {
        return (root, query, builder) -> builder.like(root.get("phone"), "%" + phone + "%");
    }
}

