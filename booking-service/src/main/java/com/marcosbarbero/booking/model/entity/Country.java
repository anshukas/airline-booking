package com.marcosbarbero.booking.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * The Country representation.
 *
 * @author Marcos Barbero
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "booking")
public class Country implements Serializable {
    private static final long serialVersionUID = -6688891731503040796L;

    @Size(min = 2, max = 2)
    @NotNull
    @Id
    @Column(unique = true, nullable = false, length = 2)
    private String code;

    @Column(nullable = false, length = 45)
    private String name;

}
