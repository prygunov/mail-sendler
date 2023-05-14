package net.artux.sendler.entity.statistic;

import net.artux.sendler.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "statistic")
public class StatisticEntity extends BaseEntity {

    private int clicks;
    private int opened;
    private int failed;

}
