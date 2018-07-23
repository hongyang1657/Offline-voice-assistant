package fitme.ai.zotyeautoassistant.bean;

/**
 * Created by hongy on 2018/1/29.
 */

public class NoiseFilter {
    private String status;
    private String intent_score;
    private String score;
    private String intent;
    private boolean result;
    private String skill_score;
    private String skill;
    private String slots_score;
    private String language_model_perplexity;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIntent_score() {
        return intent_score;
    }

    public void setIntent_score(String intent_score) {
        this.intent_score = intent_score;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getSkill_score() {
        return skill_score;
    }

    public void setSkill_score(String skill_score) {
        this.skill_score = skill_score;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getSlots_score() {
        return slots_score;
    }

    public void setSlots_score(String slots_score) {
        this.slots_score = slots_score;
    }

    public String getLanguage_model_perplexity() {
        return language_model_perplexity;
    }

    public void setLanguage_model_perplexity(String language_model_perplexity) {
        this.language_model_perplexity = language_model_perplexity;
    }
}
