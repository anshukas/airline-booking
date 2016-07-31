package com.marcosbarbero.booking.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The customer representation.
 *
 * @author Marcos Barbero
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "booking")
@EqualsAndHashCode(callSuper = true)
public class Customer extends AutoId implements Serializable {
    private static final long serialVersionUID = 7028106544314377885L;

    @JoinColumn(name = "country_code")
    @ManyToOne(fetch = FetchType.EAGER)
    private Country country;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, length = 255)
    private String fullName;

    @Column(nullable = false, length = 45)
    private String phone;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    private List<Booking> bookings = new ArrayList<>();
}
