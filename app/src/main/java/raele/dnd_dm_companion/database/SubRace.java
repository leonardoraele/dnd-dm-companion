package raele.dnd_dm_companion.database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by lpr on 21/09/15.
 */
@Table(name = "sub_race")
public class SubRace extends Model {

    @Column public Integer nameTextId;
    @Column public BaseRace superRace;
    @Column public String tallness;
    @Column public String weight;
    @Column public Size size;
    @Column public Integer adultAgeTextId;
    @Column public Integer maxAgeTextId;
    @Column public Integer alignmentTextId;
    @Column public Integer abilityModifierTextId;
    @Column public Integer speed;
    @Column public Integer languagesTextId;
    @Column public Darkvision darkvision;
    @Column public Integer namesDescriptionTextId;
    @Column public Integer namesChildTextId;
    @Column public Integer namesMaleTextId;
    @Column public Integer namesFemaleTextId;
    @Column public Integer namesFamilyTextId;
    @Column public Integer namesNicknamesTextId;

}
