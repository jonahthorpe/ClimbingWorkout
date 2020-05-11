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
                exercise = new Exercise("Knee Push Ups", "knee_push_ups_no_sound");
                exerciseDao.insert(exercise);
                exercise = new Exercise("Push Ups", "push_up_no_sound");
                exerciseDao.insert(exercise);
                exercise = new Exercise("Incline Push Ups", "incline_push_ups_no_sound");
                exerciseDao.insert(exercise);
                exercise = new Exercise("Pike Push Ups", "pike_push_up_no_sound");
                exerciseDao.insert(exercise);
                exercise = new Exercise("Wide Push Ups", "wide_push_up_no_sound");
                exerciseDao.insert(exercise);
                exercise = new Exercise("Narrow Push Ups", "narrow_push_up_no_sound");
                exerciseDao.insert(exercise);
                exercise = new Exercise("Crunches", "crunches_no_sound");
                exerciseDao.insert(exercise);
                exercise = new Exercise("Arch Ups", "arch_ups_no_sound");
                exerciseDao.insert(exercise);
                exercise = new Exercise("Plank", "plank_no_sound");
                exerciseDao.insert(exercise);
                exercise = new Exercise("Side Plank Left", "side_plank_no_sound");
                exerciseDao.insert(exercise);
                exercise = new Exercise("Side Plank Right", "side_plank_no_sound");
                exerciseDao.insert(exercise);
                exercise = new Exercise("Bicycle Crunches", "bicycle_crunch_no_sound");
                exerciseDao.insert(exercise);
                exercise = new Exercise("Repeaters", "repeaters_no_sound");
                exerciseDao.insert(exercise);


                WorkoutExerciseDao workoutExerciseDao = INSTANCE.workoutExerciseDao();
                workoutExerciseDao.deleteAll();
                WorkoutExercise workoutExercise;

                // endurance
                // pull
                WorkoutCard card = new WorkoutCard(1, "Pull", "Endurance", "@drawable/pullup");
                cardDao.insert(card);

                    // beginner
                    WorkoutDifficulty difficulty = new WorkoutDifficulty(1, 1, "Beginner", "This workout is designed to develop the necessary strength to complete more specific workouts. It was created for people who can't do any pull ups or can do only a few pull up. " +
                            "For the sets of pull ups, do as many as you can up to five. If you can't do any, you can skip this set but make sure to at least try one pull up to view progress and see if you can perform a pull up." +
                            "Once you can do three sets of five pull ups, you can try the intermediate workout.");
                    difficultyDao.insert(difficulty);
                        // exercises
                        workoutExercise = new WorkoutExercise(1, "Scapular Pull Ups", 3, "10", Utility.REPS, 120, 1, 0, 0);
                        workoutExerciseDao.insert(workoutExercise);
                        workoutExercise = new WorkoutExercise(1, "Pull Ups", 3, "max", Utility.REPS, 120, 2, 0, 0);
                        workoutExerciseDao.insert(workoutExercise);
                        workoutExercise = new WorkoutExercise(1, "Body Weight Rows", 3, "8", Utility.REPS, 120, 3, 0, 0);
                        workoutExerciseDao.insert(workoutExercise);
                        workoutExercise = new WorkoutExercise(1, "Pull Up Negatives", 3, "8", Utility.REPS, 120, 4, 0, 0);
                        workoutExerciseDao.insert(workoutExercise);

                    // advanced
                    difficulty = new WorkoutDifficulty(2, 1, "Advanced", "This workout is designed to increase you pull ups past ten. If this is too easy, you can try increasing the reps and sets for all exercises, as well as reducing rest.");
                    difficultyDao.insert(difficulty);
                        // exercise
                        workoutExercise = new WorkoutExercise(2, "Scapular Pull Ups", 3, "12", Utility.REPS, 120, 1, 0, 0);
                        workoutExerciseDao.insert(workoutExercise);
                        workoutExercise = new WorkoutExercise(2, "French Pull Ups", 4, "max", Utility.REPS, 120, 2, 0, 0);
                        workoutExerciseDao.insert(workoutExercise);
                        workoutExercise = new WorkoutExercise(2, "Pull Ups", 4, "max", Utility.REPS, 120, 3, 0, 0);
                        workoutExerciseDao.insert(workoutExercise);

                    // intermediate
                    difficulty = new WorkoutDifficulty(3, 1, "Intermediate", "This workout is designed to increase how many pull up you can do. It was created for people who find the beginner pull up routine too easy but can't do ten pull ups. " +
                            "For the sets of pull ups, do as many as you can up to ten. Once you can do three sets of ten pull ups, you can try the advanced workout.");
                    difficultyDao.insert(difficulty);
                        // exercises
                        workoutExercise = new WorkoutExercise(3, "Scapular Pull Ups", 3, "12", Utility.REPS, 120, 1, 0, 0);
                        workoutExerciseDao.insert(workoutExercise);
                        workoutExercise = new WorkoutExercise(3, "Pull Ups", 4, "max", Utility.REPS, 120, 2, 0, 0);
                        workoutExerciseDao.insert(workoutExercise);
                        workoutExercise = new WorkoutExercise(3, "Body Weight Rows", 4, "12", Utility.REPS, 120, 3, 0, 0);
                        workoutExerciseDao.insert(workoutExercise);

                // push
                card = new WorkoutCard(2, "Push", "Endurance", "@drawable/push_up");
                cardDao.insert(card);
                    // beginner
                    difficulty = new WorkoutDifficulty(4, 2, "Beginner", "This workout is designed to develop the necessary strength to complete more specific workouts. It was created for people who can only do five or less push ups. " +
                            "For the sets of push ups, do as many as you can up to five. If you can't do any, you can skip this set but make sure to at least try one push up to view progress and see if you can perform a push up." +
                            "Once you can do three sets of five push ups, you can try the intermediate workout.");
                    difficultyDao.insert(difficulty);
                        // exercises
                        workoutExercise = new WorkoutExercise(4, "Knee Push Ups", 3, "10", Utility.REPS, 120, 1, 0, 0);
                        workoutExerciseDao.insert(workoutExercise);
                        workoutExercise = new WorkoutExercise(4, "Push Ups", 3, "max", Utility.REPS, 120, 2, 0, 0);
                        workoutExerciseDao.insert(workoutExercise);
                        workoutExercise = new WorkoutExercise(4, "Incline Push Ups", 3, "8", Utility.REPS, 120, 3, 0, 0);
                        workoutExerciseDao.insert(workoutExercise);


                    // advanced
                    difficulty = new WorkoutDifficulty(5, 2, "Advanced", "This workout is designed to increase you push ups past twenty. If this is too easy, you can try increasing the reps and sets for all exercises, as well as reducing rest.");
                    difficultyDao.insert(difficulty);
                        // exercise
                        workoutExercise = new WorkoutExercise(5, "Push Ups", 3, "max", Utility.REPS, 120, 1, 0, 0);
                        workoutExerciseDao.insert(workoutExercise);
                        workoutExercise = new WorkoutExercise(5, "Pike Push Ups", 3, "15", Utility.REPS, 120, 2, 0, 0);
                        workoutExerciseDao.insert(workoutExercise);
                        workoutExercise = new WorkoutExercise(5, "Wide Push Ups", 3, "12", Utility.REPS, 120, 3, 0, 0);
                        workoutExerciseDao.insert(workoutExercise);
                        workoutExercise = new WorkoutExercise(5, "Narrow Push Ups", 3, "12", Utility.REPS, 120, 4, 0, 0);
                        workoutExerciseDao.insert(workoutExercise);


                    // intermediate
                    difficulty = new WorkoutDifficulty(6, 2, "Intermediate", "This workout is designed to increase how many push up you can do. It was created for people who find the beginner push up routine too easy but can't do fifteen push ups. " +
                            "For the sets of push ups, do as many as you can up to fifteen. Once you can do three sets of fifteen push ups, you can try the advanced workout.");
                    difficultyDao.insert(difficulty);
                        // exercises
                        workoutExercise = new WorkoutExercise(6, "Push Ups", 3, "max - 2", Utility.REPS, 120, 1, 0, 0);
                        workoutExerciseDao.insert(workoutExercise);
                        workoutExercise = new WorkoutExercise(6, "Pike Push Ups", 3, "8", Utility.REPS, 120, 2, 0, 0);
                        workoutExerciseDao.insert(workoutExercise);
                        workoutExercise = new WorkoutExercise(6, "Wide Push Ups", 2, "max - 2", Utility.REPS, 120, 3, 0, 0);
                        workoutExerciseDao.insert(workoutExercise);
                        workoutExercise = new WorkoutExercise(6, "Narrow Push Ups", 2, "max - 2", Utility.REPS, 120, 4, 0, 0);
                        workoutExerciseDao.insert(workoutExercise);

            // core
            card = new WorkoutCard(3, "Core", "Endurance", "@drawable/sit_up");
            cardDao.insert(card);
                // beginner
                difficulty = new WorkoutDifficulty(7, 3, "Beginner", "This workout is designed to develop the necessary strength to complete more specific workouts. It was created for people who new to core training. " +
                        "For the timed exercises, try your best to complete the full time but don't worry if you can't." +
                        "Once you can comfortably complete this workout, you can move on to the intermediate workout.");
                difficultyDao.insert(difficulty);
                    // exercises
                    workoutExercise = new WorkoutExercise(7, "Crunches", 3, "10", Utility.REPS, 120, 1, 0, 0);
                    workoutExerciseDao.insert(workoutExercise);
                    workoutExercise = new WorkoutExercise(7, "Arch Ups", 3, "30", Utility.TIME, 120, 2, 0, 0);
                    workoutExerciseDao.insert(workoutExercise);
                    workoutExercise = new WorkoutExercise(7, "Plank", 3, "30", Utility.TIME, 120, 3, 0, 0);
                    workoutExerciseDao.insert(workoutExercise);
                    workoutExercise = new WorkoutExercise(7, "Side Plank Left", 2, "30", Utility.TIME, 120, 4, 0, 0);
                    workoutExerciseDao.insert(workoutExercise);
                    workoutExercise = new WorkoutExercise(7, "Side Plank Right", 2, "30", Utility.TIME, 120, 5, 0, 0);
                    workoutExerciseDao.insert(workoutExercise);



                // advanced
                difficulty = new WorkoutDifficulty(8, 3, "Advanced", "This workout is developed for people very comfortable with core training. It is designed to improve how long you can user your core for while climbing." +
                        " To make this workout harder, you can increase the amount of set and reps you complete, as well as reducing the amount of rest you take.");
                difficultyDao.insert(difficulty);
                    // exercise
                    workoutExercise = new WorkoutExercise(8, "Bicycle Crunches", 3, "45", Utility.TIME, 120, 1, 0, 0);
                    workoutExerciseDao.insert(workoutExercise);
                    workoutExercise = new WorkoutExercise(8, "Arch Ups", 3, "60", Utility.TIME, 120, 2, 0, 0);
                    workoutExerciseDao.insert(workoutExercise);
                    workoutExercise = new WorkoutExercise(8, "Plank", 3, "60", Utility.TIME, 120, 3, 0, 0);
                    workoutExerciseDao.insert(workoutExercise);
                    workoutExercise = new WorkoutExercise(8, "Side Plank Left", 2, "60", Utility.TIME, 120, 4, 0, 0);
                    workoutExerciseDao.insert(workoutExercise);
                    workoutExercise = new WorkoutExercise(8, "Side Plank Right", 2, "60", Utility.TIME, 120, 5, 0, 0);
                    workoutExerciseDao.insert(workoutExercise);



                // intermediate
                difficulty = new WorkoutDifficulty(9, 3, "Intermediate", "This workout is developed for people somewhat comfortable with core training. It is designed to improve how long you can user your core for while climbing." +
                        "Once you can complete this workout comfortably, you can move onto the advanced workout.");
                difficultyDao.insert(difficulty);
                    // exercises
                    workoutExercise = new WorkoutExercise(9, "Bicycle Crunches", 3, "30", Utility.TIME, 120, 1, 0, 0);
                    workoutExerciseDao.insert(workoutExercise);
                    workoutExercise = new WorkoutExercise(9, "Arch Ups", 3, "45", Utility.TIME, 120, 2, 0, 0);
                    workoutExerciseDao.insert(workoutExercise);
                    workoutExercise = new WorkoutExercise(9, "Plank", 3, "45", Utility.TIME, 120, 3, 0, 0);
                    workoutExerciseDao.insert(workoutExercise);
                    workoutExercise = new WorkoutExercise(9, "Side Plank Left", 2, "45", Utility.TIME, 120, 4, 0, 0);
                    workoutExerciseDao.insert(workoutExercise);
                    workoutExercise = new WorkoutExercise(9, "Side Plank Right", 2, "45", Utility.TIME, 120, 5, 0, 0);
                    workoutExerciseDao.insert(workoutExercise);

            // fingers
            card = new WorkoutCard(4, "Fingers", "Endurance", "@drawable/hangboard_end");
            cardDao.insert(card);
                // beginner
                difficulty = new WorkoutDifficulty(10, 4, "Beginner", "This workout is designed to develop the necessary strength to complete more specific workouts as well as to get use to hangboarding. It was created for people who new to hangboarding. Use an edge size around 28 mm. " +
                        "You should use variety grips types through out the workout, such as : 4 fingers crimp, 4 fingers open, front 3 open and back 3 open. If this is too difficult, you can reduce the amount sets you perform as well reducing your weight with a pulley system.");
                difficultyDao.insert(difficulty);
                    // exercises
                    workoutExercise = new WorkoutExercise(10, "Repeaters", 3, "6", Utility.REPEATERS, 120, 1, 7, 3);
                    workoutExerciseDao.insert(workoutExercise);



                // advanced
                difficulty = new WorkoutDifficulty(11, 4, "Advanced", "This workout is developed for people very comfortable with hangboarding. It is designed to improve how long you can stay on the wall before you get pumped out." +
                        " Use an edge size around 20 mm. You should use variety grips types through out the workout, such as : 4 fingers crimp, 4 fingers open, front 3 open and back 3 open. To make this harder, you can increase the amount of set or reps you complete, " +
                        "as well as adding weight to your body.");
                difficultyDao.insert(difficulty);
                    // exercise
                    workoutExercise = new WorkoutExercise(11, "Repeaters", 7, "6", Utility.REPEATERS, 120, 1, 7, 3);
                    workoutExerciseDao.insert(workoutExercise);



                // intermediate
                difficulty = new WorkoutDifficulty(12, 4, "Intermediate", "This workout is developed for people somewhat comfortable with hangboarding. It is designed to improve how long you can stay on the wall before you get pumped out." +
                        " Use an edge size around 25 mm. You should use variety grips types through out the workout, such as : 4 fingers crimp, 4 fingers open, front 3 open and back 3 open. Once you can complete this workout comfortably, you can move onto the advanced workout.");
                difficultyDao.insert(difficulty);
                    // exercises
                    workoutExercise = new WorkoutExercise(12, "Repeaters", 5, "6", Utility.REPEATERS, 120, 1, 7, 3);
                    workoutExerciseDao.insert(workoutExercise);



            // strength
            // pull
            card = new WorkoutCard(5, "Pull", "Strength", "@drawable/onearmtemp");
            cardDao.insert(card);
                // beginner
                difficulty = new WorkoutDifficulty(13, 5, "Beginner", "This workout is designed to develop the necessary strength to complete more specific workouts. It was created for people who can't do any pull ups or can do only a few pull up. " +
                        "For the sets of pull ups, do as many as you can up to five. If you can't do any, you can skip this set but make sure to at least try one pull up to view progress and see if you can perform a pull up." +
                        "Once you can do three sets of five pull ups, you can try the intermediate workout.");
                difficultyDao.insert(difficulty);
                    // exercises
                    workoutExercise = new WorkoutExercise(13, "Scapular Pull Ups", 3, "10", Utility.REPS, 120, 1, 0, 0);
                    workoutExerciseDao.insert(workoutExercise);
                    workoutExercise = new WorkoutExercise(13, "Pull Ups", 3, "max", Utility.REPS, 120, 2, 0, 0);
                    workoutExerciseDao.insert(workoutExercise);
                    workoutExercise = new WorkoutExercise(13, "Body Weight Rows", 3, "8", Utility.REPS, 120, 3, 0, 0);
                    workoutExerciseDao.insert(workoutExercise);
                    workoutExercise = new WorkoutExercise(13, "Pull Up Negatives", 3, "8", Utility.REPS, 120, 4, 0, 0);
                    workoutExerciseDao.insert(workoutExercise);


                // advanced
                difficulty = new WorkoutDifficulty(14, 5, "Advanced", "This workout is designed to increase your overall pulling strength. It is created for people who find doing five pull ups with 5 kg added to easy. To progress in this workout," +
                        " you can gradually increase weight to keep the reps you perform around five. You can also try learn some hard body weight exercises like the front lever and one arm pull up. If you exercising at home and don't have weights, you can use water bottles.");
                difficultyDao.insert(difficulty);
                // exercise



                // intermediate
                difficulty = new WorkoutDifficulty(15, 5, "Intermediate", "This workout is designed to increase your overall strength with pulling movements. It was created for people who find the beginner pull up routine too easy but can't do five pull ups with 5kg added." +
                        " For the sets of pull ups, do as many as you can up to five with the weight added. Once you can do three sets of five pull ups, you can try the advanced workout. When adding, start with 1kg and gradually move up to 5. " +
                        "If you exercising at home and don't have weights, you can use water bottles.");
                difficultyDao.insert(difficulty);
                // exercises

            // push
            card = new WorkoutCard(6, "Push", "Strength", "@drawable/decline_push_up");
            cardDao.insert(card);
                // beginner
                difficulty = new WorkoutDifficulty(16, 6, "Beginner", "This workout is designed to develop the necessary strength to complete more specific workouts. It was created for people who can only do five or less push ups. " +
                        "For the sets of push ups, do as many as you can up to five. If you can't do any, you can skip this set but make sure to at least try one push up to view progress and see if you can perform a push up." +
                        "Once you can do three sets of five push ups, you can try the intermediate workout.");
                difficultyDao.insert(difficulty);
                    // exercises
                    workoutExercise = new WorkoutExercise(16, "Knee Push Ups", 3, "10", Utility.REPS, 120, 1, 0, 0);
                    workoutExerciseDao.insert(workoutExercise);
                    workoutExercise = new WorkoutExercise(16, "Push Ups", 3, "max", Utility.REPS, 120, 2, 0, 0);
                    workoutExerciseDao.insert(workoutExercise);
                    workoutExercise = new WorkoutExercise(16, "Incline Push Ups", 3, "8", Utility.REPS, 120, 3, 0, 0);
                    workoutExerciseDao.insert(workoutExercise);



                // advanced
                difficulty = new WorkoutDifficulty(17, 6, "Advanced", "This workout is designed to increase your overall pushing strength. It is created for people who find doing five push ups with 5 kg added to easy. To progress in this workout," +
                        " you can gradually increase weight to keep the reps you perform around five. You can also try learn some hard body weight exercises like the handstands and planches. If you exercising at home and don't have weights, you can use water bottles or " +
                        "partner assistance.");
                difficultyDao.insert(difficulty);
                // exercise



                // intermediate
                difficulty = new WorkoutDifficulty(18, 6, "Intermediate", "This workout is designed to increase your overall strength with pushing movements. It was created for people who find the beginner push up routine too easy but can't do five push ups with 5kg added." +
                        " For the sets of push ups, do as many as you can up to five with the weight added. Once you can do three sets of five push ups, you can try the advanced workout. When adding, start with 1kg and gradually move up to 5. " +
                        "If you exercising at home and don't have weights, you can use water bottles. You also use partner assistance");
                difficultyDao.insert(difficulty);
                // exercises

            // core
            card = new WorkoutCard(7, "Core", "Strength", "@drawable/bruce_lee_dragon_flags");
            cardDao.insert(card);
                // beginner
                difficulty = new WorkoutDifficulty(19, 7, "Beginner", "This workout is designed to develop the necessary strength to complete more specific workouts. It was created for people who new to core training. " +
                        "For the timed exercises, try your best to complete the full time but don't worry if you can't." +
                        "Once you can comfortably complete this workout, you can move on to the intermediate workout.");
                difficultyDao.insert(difficulty);
                // exercises


                // advanced
                difficulty = new WorkoutDifficulty(20, 7, "Advanced", "This workout is developed for people very comfortable with core training. It is designed to improve how tension on the wall build overall strength of the core." +
                        " To make this workout harder, you can increase the amount of set and reps you complete. You can also add weight or raise your legs when applicable.");
                difficultyDao.insert(difficulty);
                // exercise



                // intermediate
                difficulty = new WorkoutDifficulty(21, 7, "Intermediate", "This workout is developed for people somewhat comfortable with core training. It is designed to improve how tension on the wall build overall strength of the core." +
                        "Once you can complete this workout comfortably, you can move onto the advanced workout.");
                difficultyDao.insert(difficulty);
                // exercises

            // fingers
            card = new WorkoutCard(8, "Finger", "Strength", "@drawable/alexhonnoldhangboard");
            cardDao.insert(card);
                // beginner
                difficulty = new WorkoutDifficulty(22, 8, "Beginner", "This workout is designed to develop the necessary strength to complete more specific workouts as well as to get use to hangboarding. It was created for people who new to hangboarding. Use an edge size around 20 mm. " +
                        "You should use variety grips types through out the workout, such as : 4 fingers crimp, 4 fingers open, front 3 open and back 3 open. If this is too difficult, you can reduce the amount sets you perform as well reducing your weight with a pulley system.");
                difficultyDao.insert(difficulty);
                    // exercises



                // advanced
                difficulty = new WorkoutDifficulty(23, 8, "Advanced", "This workout is developed for people very comfortable with hangboarding. It is designed to help you hold on to smaller holds and steeper angels." +
                        " Use an edge size around 20 mm. You should use variety grips types through out the workout, such as : 4 fingers crimp, 4 fingers open, front 3 open and back 3 open. To make this harder, you can increase the amount of sets you complete, " +
                        "as well as adding weight to your body. You can also try different style of strength workouts such as one arm hangs.");
                difficultyDao.insert(difficulty);
                    // exercise



                // intermediate
                difficulty = new WorkoutDifficulty(24, 8, "Intermediate", "This workout is developed for people somewhat comfortable with hangboarding. It is designed to help you hold on to smaller holds and steeper angels." +
                        " Use an edge size around 20 mm. You should use variety grips types through out the workout, such as : 4 fingers crimp, 4 fingers open, front 3 open and back 3 open. Once you can complete this workout comfortably, you can move onto the advanced workout.");
                difficultyDao.insert(difficulty);
                    // exercises

            // power
            // pull
            card = new WorkoutCard(9, "Pull", "Power", "@drawable/campus_board_training");
            cardDao.insert(card);
                // beginner
                difficulty = new WorkoutDifficulty(25, 9, "Beginner", "This workout is designed to increase you power in pulling motions. It is recommended to be able to complete the intermediate strength workout before starting this. This workout will help with dynamic and big" +
                        " moves while climbing. If this is to hard, try build some foundation strength with the strength workouts as well as lowering the overall volume. When you can comfortably complete this workout, you can move onto the intermediate one.");
                difficultyDao.insert(difficulty);
                    // exercises



                // advanced
                difficulty = new WorkoutDifficulty(26, 9, "Advanced", "This workout is designed to increase you power in pulling motions. This workout will help with dynamic and big moves while climbing. if this is too easy, you can try adding weights" +
                        " or experiment with new explosive exercises.");
                difficultyDao.insert(difficulty);
                    // exercise



                // intermediate
                difficulty = new WorkoutDifficulty(27, 9, "Intermediate", "This workout is designed to increase you power in pulling motions. This workout will help with dynamic and big moves while climbing. If this is too hard you can try decreasing volume and increasing " +
                        "rest time. When you can comfortably complete this workout, you can move onto the advanced one.");
                difficultyDao.insert(difficulty);
                    // exercises

            // push
            card = new WorkoutCard(10, "Push", "Power", "@drawable/plyometric_pushups");
            cardDao.insert(card);
                // beginner
                difficulty = new WorkoutDifficulty(28, 10, "Beginner", "This workout is designed to increase you power in pushing motions. It is recommended to be able to complete the intermediate strength workout before starting this. This workout will help with dynamic and big" +
                        " moves while climbing. If this is to hard, try build some foundation strength with the strength workouts as well as lowering the overall volume. When you can comfortably complete this workout, you can move onto the intermediate one.");
                difficultyDao.insert(difficulty);
                    // exercises



                // advanced
                difficulty = new WorkoutDifficulty(29, 10, "Advanced", "This workout is designed to increase you power in pushing motions. This workout will help with dynamic and big moves while climbing. if this is too easy, you can try adding weights" +
                        " or experiment with new explosive exercises.");
                difficultyDao.insert(difficulty);
                    // exercise



                // intermediate
                difficulty = new WorkoutDifficulty(39, 10, "Intermediate", "This workout is designed to increase you power in pushing motions. This workout will help with dynamic and big moves while climbing. If this is too hard you can try decreasing volume and increasing " +
                        "rest time. When you can comfortably complete this workout, you can move onto the advanced one.");
                difficultyDao.insert(difficulty);
                    // exercises
            // core
            card = new WorkoutCard(11, "Core", "Power", "@drawable/kettle_bell_swing");
            cardDao.insert(card);
                // beginner
                difficulty = new WorkoutDifficulty(31, 11, "Beginner", "This workout is designed to increase you power in your core area. It is recommended to be able to complete the beginner strength workout before starting this. This workout will help with dynamic and big" +
                        " moves while climbing. If this is to hard, try build some foundation strength with the strength workouts as well as lowering the overall volume. When you can comfortably complete this workout, you can move onto the intermediate one.");
                difficultyDao.insert(difficulty);
                // exercises



                // advanced
                difficulty = new WorkoutDifficulty(32, 11, "Advanced", "This workout is designed to increase you power iin your core area. This workout will help with dynamic and big moves while climbing. if this is too easy, you can try adding weights" +
                        " or experiment with new explosive exercises.");
                difficultyDao.insert(difficulty);
                // exercise



                // intermediate
                difficulty = new WorkoutDifficulty(33, 11, "Intermediate", "This workout is designed to increase you power in your core area. This workout will help with dynamic and big moves while climbing. If this is too hard you can try decreasing volume and increasing " +
                        "rest time. When you can comfortably complete this workout, you can move onto the advanced one.");
                difficultyDao.insert(difficulty);
                // exercises
        });
        }
    };

}
