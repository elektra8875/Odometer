package com.example.cindylou.workout;

public class Workout {
    private String name;
    private String description;

    public static final Workout [] workouts = {
            new Workout("The limb lossner", "5 pushups\n10 Pullups"),
            new Workout("Core Agoney", "100 Pulldowns\n100 Squats"),
            new Workout("The Wimp Special", "5 pullups\n10 Squats"),
            new Workout("Strength and length", "550 meter\n21 ")

    };

    //each workout has a name and description
private Workout(String name, String description) {
    this.name = name;
    this.description = description;

}

public String getDescription() {
    return description;
    }

public String getName(){
    return name;

}

public String toString() {
    return this.name;
}

}
