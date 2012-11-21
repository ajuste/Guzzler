/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guzzler.model;

import com.guzzler.common.IEntity;
import java.util.UUID;

/**
 *
 * @author ajuste
 */
public class CharacterInfo implements IEntity {

    private UUID id = null;
    private String name = "";
    private int score = 0;
    private int level = 1;
    private int experience = 0;
    private float health = 1.0f;
    private float fun = 0.75f;
    private float food = 0.75f;
    private float energy = 0.75f;
    private float poo = 0.0f;
    private boolean playing = false;
    private CharacterTemplateInfo template = new CharacterTemplateInfo();

    public CharacterInfo() {
    }

    public CharacterInfo(String name, CharacterTemplateInfo template) {
        this.name = name;
        this.template = template;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getFun() {
        return fun;
    }

    public void setFun(float fun) {
        this.fun = fun;
    }

    public float getFood() {
        return food;
    }

    public void setFood(float food) {
        this.food = food;
    }

    public float getEnergy() {
        return energy;
    }

    public void setEnergy(float energy) {
        this.energy = energy;
    }

    public float getPoo() {
        return poo;
    }

    public void setPoo(float poo) {
        this.poo = poo;
    }

    public CharacterTemplateInfo getTemplate() {
        return template;
    }

    public void setTemplate(CharacterTemplateInfo template) {
        this.template = template;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return this.id;
    }
}