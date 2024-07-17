package com.gfa.tribesvibinandtribinotocyon.models.roles;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("admin")
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RoleAdmin extends RoleUser {

}