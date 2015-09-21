package raele.dnd_dm_companion.database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by lpr on 21/09/15.
 */
@Table(name = "base_race")
public class BaseRace extends Model {

    @Column public Integer nameId;

}
