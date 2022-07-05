package net.artux.mupse.entity.statistic;

import net.artux.mupse.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "statistic")
public class StatisticEntity extends BaseEntity {

    private int clicks;
    private int opened;

}
