
package snailstrike.utils;


public class Path {

    public static abstract class Flow {

        private String path;

        public Flow(String path) {
            this.path = path;
        }

        public Flow(Flow flow, String path) {
            this.path = flow.path + path;
        }

        @Override
        public String toString() {
            return path;
        }
    }

    private static class Resources extends Flow {

        private Resources() {
            super("/resources");
        }
    }

    public static class Imgs extends Flow {

        private Imgs() {
            super(new Resources(), "/imgs");
        }

        public static class Actors extends Flow {

            private Actors(Flow flow) {
                super(flow, "/actors");
            }

            public String aircraft() {
                return this + "/airplane1.png";
            }

            public String snail() {
                return this + "/snail.png";
            }

            public String enemy() {
                return this + "/enemy1.png";
            }

            public String actor() {
                return this + "/Actor1.png";
            }
            
             public String burn() {
                return this + "/burn.png";
            }
            
        }

        public static class Props extends Flow {

            private Props(Flow flow) {
                super(flow, "/props");
            }

            public String bombNormal() {
                return this + "/BombNormal.png";
            }

            public String explosion_sp() {
                return this + "/explosion_sp.png";
            }

            public String trapLeaves() {
                return this + "/trapLeaves.png";
            }

            public String trapExplose() {
                return this + "/trapExplose.png";
            }

            public String ExplodeAtOnce() {
                return this + "/ExplodeAtOnce.png";
            }

            public String ExplodeAtOnceExplose() {
                return this + "/ExplodeAtOnceExplose.png";
            }

            public String Tarck() {
                return this + "/Tarck.png";
            }

            public String BoxExist() {
                return this + "/BoxExist.png";
            }

            public String Defend() {
                return this + "/Defend.png";
            }

            public String Meteorite() {
                return this + "/Meteorite.png";
            }

            public String MeteoriteExplose() {
                return this + "/MeteoriteExplose.png";
            }

            public String MeteoriteRollExplose() {
                return this + "/MeteoriteRollExplose.png";
            }

            public String MeteoriteRoll() {
                return this + "/MeteoriteRoll.png";
            }

            public String Bump() {
                return this + "/Bump.png";
            }

            public String BlackHoleBoom() {
                return this + "/BlackHoleBoom.png";
            }

            public String BlackHoleNormal() {
                return this + "/BlackHoleNormal.png";
            }
        }

        public static class Skills extends Flow {

            private Skills(Flow flow) {
                super(flow, "/skills");
            }

            public String bubble() {
                return this + "/bubble.png";
            }

            public String smog() {
                return this + "/smog.png";
            }

            public String water() {
                return this + "/water.png";
            }

            public String wind() {
                return this + "/wind.png";
            }

            public String fire() {
                return this + "/fire.png";
            }

            public String enrichblood() {
                return this + "/enrichblood.png";
            }

            public String teleportation() {
                return this + "/teleportation.png";
            }

            public String velocity() {
                return this + "/velocity.png";
            }

            public String boomerang() {
                return this + "/boomerang.png";
            }

            public String track() {
                return this + "/track.png";
            }

            public String attackSplit_1() {
                return this + "/attackSplit_1.png";
            }

            public String attackSplit_2() {
                return this + "/attackSplit_2.png";
            }

            public String attackSplit_3() {
                return this + "/attackSplit_3.png";
            }

            public String attackRange_1() {
                return this + "/attackRange_1.png";
            }

            public String attackRange_2() {
                return this + "/attackRange_2.png";
            }

            public String attackRange_3() {
                return this + "/attackRange_3.png";
            }

            public String attackForce_1() {
                return this + "/attackForce_1.png";
            }

            public String attackForce_2() {
                return this + "/attackForce_2.png";
            }

            public String attackForce_3() {
                return this + "/attackForce_3.png";
            }

            public String boomerang_1() {
                return this + "/boomerang_1.png";
            }

            public String boomerang_2() {
                return this + "/boomerang_2.png";
            }

            public String boomerang_3() {
                return this + "/boomerang_3.png";
            }

            public String enrichBlood_1() {
                return this + "/enrichBlood_1.png";
            }

            public String enrichBlood_2() {
                return this + "/enrichBlood_2.png";
            }

            public String enrichBlood_3() {
                return this + "/enrichBlood_3.png";
            }

            public String velocity_1() {
                return this + "/velocity_1.png";
            }

            public String velocity_2() {
                return this + "/velocity_2.png";
            }

            public String velocity_3() {
                return this + "/velocity_3.png";
            }

            public String teleportation_1() {
                return this + "/teleportation_1.png";
            }

            public String teleportation_2() {
                return this + "/teleportation_2.png";
            }

            public String teleportation_3() {
                return this + "/teleportation_3.png";
            }

            public String invisible_1() {
                return this + "/invisible_1.png";
            }

            public String invisible_2() {
                return this + "/invisible_2.png";
            }

            public String invisible_3() {
                return this + "/invisible_3.png";
            }
        }

        public static class Map extends Flow {

            private Map(Flow flow) {
                super(flow, "/map");
            }

            public String mud() {
                return this + "/mud.png";
            }

            public String mud2() {
                return this + "/mud2.png";
            }

            public String lavaAll() {
                return this + "/lavaAll.png";
            }

            public String lavaOfAll2() {
                return this + "/lavaOfAll2.png";
            }

            public String mapSpellAll() {
                return this + "/mapSpellAll.png";
            }

            public String waterbackground() {
                return this + "/waterbackground.png";
            }
        }

        public static class Objs extends Flow {

            private Objs(Flow flow) {
                super(flow, "/objs");
            }

            public String boom() {
                return this + "/boom.png";
            }

            public String youSurvived() {
                return this + "/youSurvived.png";
            }

            public String youDied() {
                return this + "/youDied.png";
            }

            public String back() {
                return this + "/back.png";
            }

            public String backToMain() {
                return this + "/backToMain.png";
            }

            public String confirm() {
                return this + "/confirm.png";
            }

            public String exit() {
                return this + "/exit.png";
            }

            public String instruction() {
                return this + "/instruction.png";
            }

            public String introduction() {
                return this + "/introduction.png";
            }

            public String itemIntroduction() {
                return this + "/itemIntroduction.png";
            }

            public String playAgain() {
                return this + "/playAgain.png";
            }

            public String seeMe() {
                return this + "/seeMe.png";
            }

            public String start() {
                return this + "/start.png";
            }

        }

        public static class Backgrounds extends Flow {

            private Backgrounds(Flow flow) {
                super(flow, "/backgrounds");
            }

            public String startScreen() {
                return this + "/startScreen.png";
            }

            public String EndScreen() {
                return this + "/BoardFull.png";
            }

            public String Introduction() {
                return this + "/Introduction.png";
            }

            public String Intstruction() {
                return this + "/Intstruction.png";
            }

            public String ItemsIntroduction() {
                return this + "/ItemsIntroduction.png";
            }

            public String InputScene() {
                return this + "/inputScene.jpg";
            }

            public String ChooseSkillScene() {
                return this + "/chooseSkillScene.png";
            }

            public String HintScene() {
                return this + "/hintScene.png";
            }

            public String startScreenCloud() {
                return this + "/startScreenCloud.png";
            }

            public String title() {
                return this + "/title.png";
            }

            public String snailsheetright() {
                return this + "/snailsheetright.png";
            }

            public String snailfacerightall() {
                return this + "/snailfacerightall.png";
            }
        }

        public Actors actors() {
            return new Actors(this);
        }

        public Objs objs() {
            return new Objs(this);
        }

        public Props props() {
            return new Props(this);
        }

        public Skills skills() {
            return new Skills(this);
        }

        public Backgrounds backgrounds() {
            return new Backgrounds(this);
        }

        public Map map() {
            return new Map(this);
        }
    }

    public static class Sounds extends Flow {

        private Sounds() {
            super(new Resources(), "/sounds");
        }

        public String Alarm01() {
            return this + "/Alarm01.wav";
        }

        public String GameStartMusic() {
            return this + "/GameStartMusic.wav";
        }

        public String EndScene() {
            return this + "/EndScene.wav";
        }

        public String StartScene() {
            return this + "/StartScene.wav";
        }

        public static class Items extends Flow {

            private Items(Flow flow) {
                super(flow, "/items");
            }

            public String ItemsLikeCrazyBird() {
                return this + "/ItemsLikeCrazyBird.wav";
            }

            public String ItemsLongBoom() {
                return this + "/ItemsLongBoom.wav";
            }

            public String ItemsThrowSwipe() {
                return this + "/ItemsThrowSwipe.wav";
            }

            public String ItemsTrapBoom() {
                return this + "/ItemsTrapBoom.wav";
            }

            public String ItemsStonesRollingInWater() {
                return this + "/ItemsStonesRollingInWater.wav";
            }
        }

        public static class Skills extends Flow {

            private Skills(Flow flow) {
                super(flow, "/skills");
            }

            public String skillsSwipe() {
                return this + "/skillsSwipe.wav";
            }

            public String skillsTileDownBoom() {
                return this + "/skillsTileDownBoom.wav";
            }

        }
          public static class Obj extends Flow {

            private Obj(Flow flow) {
                super(flow, "/obj");
            }

            public String burnning() {
                return this + "/burnning.wav";
            }

            public String buttonClick() {
                return this + "/buttonClick.wav";
            }

        }

        public Items items() {
            return new Items(this);
        }

        public Skills skills() {
            return new Skills(this);
        }
          public Obj obj() {
            return new Obj(this);
        }
    }

    public Imgs img() {
        return new Imgs();
    }

    public Sounds sounds() {
        return new Sounds();
    }
}
