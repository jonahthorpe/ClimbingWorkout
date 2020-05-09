package com.example.climbingworkout;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {WorkoutCard.class, WorkoutDifficulty.class, Exercise.class, WorkoutExercise.class}, version = 1, exportSchema = false)
public abstract class WorkoutsDatabase extends RoomDatabase {

    public abstract  WorkoutCardDao workoutCardDao();
    public abstract  WorkoutDifficultyDao workoutDifficultyDao();
    public abstract  ExerciseDao exerciseDao();
    public abstract WorkoutExerciseDao workoutExerciseDao();
    private static volatile WorkoutsDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static WorkoutsDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (WorkoutsDatabase.class){
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WorkoutsDatabase.class, "workout_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                WorkoutCardDao cardDao = INSTANCE.workoutCardDao();
                cardDao.deleteAll();
                WorkoutDifficultyDao difficultyDao = INSTANCE.workoutDifficultyDao();
                difficultyDao.deleteAll();
                ExerciseDao exerciseDao = INSTANCE.exerciseDao();
                exerciseDao.deleteAll();
                Exercise exercise = new Exercise("Pull Ups", "pull_up_no_sound");
                exerciseDao.insert(exercise);
                exercise = new Exercise("Scapular Pull Ups", "scapular_pull_up_no_sound");
                exerciseDao.insert(exercise);
                exercise = new Exercise("Body Weight Rows", "body_weight_row_no_sound");
                exerciseDao.insert(exercise);
                exercise = new Exercise("Pull Up Negatives", "negative_pull_up_no_sound");
                exerciseDao.insert(exercise);
                exercise = new Exercise("French Pull Ups", "frenchies_no_sound");
                exerciseDao.insert(exercise);
                WorkoutExerciseDao workoutExerciseDao = INSTANCE.workoutExerciseDao();
                workoutExerciseDao.deleteAll();
                WorkoutExercise workoutExercise = new WorkoutExercise(1, "Scapular Pull Ups", 3, "10", 0, 120, 1, 0, 0);

                // strength
                // pull
                WorkoutCard card = new WorkoutCard(1, "Pull", "Strength", "@drawable/onearmtemp");
                cardDao.insert(card);
                card = new WorkoutCard(2, "Push", "Strength", "@drawable/decline_push_up");
                cardDao.insert(card);
                card = new WorkoutCard(3, "Core", "Strength", "@drawable/bruce_lee_dragon_flags");
                cardDao.insert(card);
                card = new WorkoutCard(10, "Finger", "Strength", "@drawable/alexhonnoldhangboard");
                    WorkoutDifficulty difficulty = new WorkoutDifficulty(4, 10, "Beginner", "");
                    difficultyDao.insert(difficulty);
                        workoutExercise = new WorkoutExercise(4, "Scapular Pull Ups", 3, "10", 1, 12, 1, 0, 0);
                        workoutExerciseDao.insert(workoutExercise);
                    difficulty = new WorkoutDifficulty(5, 1, "Intermediate", "");
                    difficultyDao.insert(difficulty);
                    difficulty = new WorkoutDifficulty(6, 1, "Advanced", "");
                    difficultyDao.insert(difficulty);
                // power
                cardDao.insert(card);
                card = new WorkoutCard(4, "Pull", "Power", "@drawable/campus_board_training");
                cardDao.insert(card);
                card = new WorkoutCard(5, "Push", "Power", "@drawable/plyometric_pushups");
                cardDao.insert(card);
                card = new WorkoutCard(6, "Core", "Power", "@drawable/kettle_bell_swing");
                cardDao.insert(card);
                // endurance
                // pull
                card = new WorkoutCard(7, "Pull", "Endurance", "@drawable/pullup");
                cardDao.insert(card);
                    difficulty = new WorkoutDifficulty(1, 7, "Beginner", "This workout is designed to develop the necessary strength to complete more specific workouts. It was created for people who can't do any pull ups or can do only a few pull up. " +
                            "For the sets of pull ups, do as many as you can up to five. If you can't do any, you can skip this set but make sure to at least try one pull up to view progress and see if you can perform a pull up." +
                            "Once you can do three sets of five pull ups, you can try the intermediate workout.");
                    difficultyDao.insert(difficulty);
                        workoutExercise = new WorkoutExercise(1, "Scapular Pull Ups", 3, "10", 0, 120, 1, 0, 0);
                        workoutExerciseDao.insert(workoutExercise);
                        workoutExercise = new WorkoutExercise(1, "Pull Ups", 3, "max", 0, 120, 2, 0, 0);
                        workoutExerciseDao.insert(workoutExercise);
                        workoutExercise = new WorkoutExercise(1, "Body Weight Rows", 3, "8", 0, 120, 3, 0, 0);
                        workoutExerciseDao.insert(workoutExercise);
                        workoutExercise = new WorkoutExercise(1, "Pull Up Negatives", 3, "8", 0, 120, 4, 0, 0);
                        workoutExerciseDao.insert(workoutExercise);


                    difficulty = new WorkoutDifficulty(2, 7, "Advanced", "This workout is designed to increase you pull past ten. If this is too easy, you can try increasing the reps and sets for all exercises, as well as reducing rest.");
                    difficultyDao.insert(difficulty);
                        workoutExercise = new WorkoutExercise(2, "Scapular Pull Ups", 3, "12", 0, 120, 1, 0, 0);
                        workoutExerciseDao.insert(workoutExercise);
                        workoutExercise = new WorkoutExercise(2, "French Pull Ups", 4, "max", 0, 120, 2, 0, 0);
                        workoutExerciseDao.insert(workoutExercise);
                        workoutExercise = new WorkoutExercise(2, "Pull Ups", 4, "max", 0, 120, 3, 0, 0);
                        workoutExerciseDao.insert(workoutExercise);



                    difficulty = new WorkoutDifficulty(3, 7, "Intermediate", "This workout is designed to increase how many pull up you can do. It was created for people who find the beginner pull up too easy but can't do ten pull ups. " +
                            "For the sets of pull ups, do as many as you can up to ten. Once you can do three sets of ten pull ups, you can try the advanced workout.");
                    difficultyDao.insert(difficulty);
                        workoutExercise = new WorkoutExercise(3, "Scapular Pull Ups", 3, "12", 0, 120, 1, 0, 0);
                        workoutExerciseDao.insert(workoutExercise);
                        workoutExercise = new WorkoutExercise(3, "Pull Ups", 4, "max", 0, 120, 2, 0, 0);
                        workoutExerciseDao.insert(workoutExercise);
                        workoutExercise = new WorkoutExercise(3, "Body Weight Rows", 4, "12", 0, 120, 3, 0, 0);
                        workoutExerciseDao.insert(workoutExercise);
                card = new WorkoutCard(8, "Push", "Endurance", "@drawable/push_up");
                cardDao.insert(card);
                card = new WorkoutCard(9, "Core", "Endurance", "@drawable/sit_up");
                cardDao.insert(card);
                card = new WorkoutCard(11, "Fingers", "Endurance", "@drawable/hangboard_end");
                cardDao.insert(card);
                    difficulty = new WorkoutDifficulty(7, 11, "Beginner", "");
                    difficultyDao.insert(difficulty);
                        workoutExercise = new WorkoutExercise(7, "Scapular Pull Ups", 3, "3", 2, 12, 1, 7, 3);
                        workoutExerciseDao.insert(workoutExercise);
                    difficulty = new WorkoutDifficulty(8, 11, "Intermediate", "");
                    difficultyDao.insert(difficulty);
                    difficulty = new WorkoutDifficulty(9, 11, "Advanced", "");
                    difficultyDao.insert(difficulty);
            });
        }
    };

}
