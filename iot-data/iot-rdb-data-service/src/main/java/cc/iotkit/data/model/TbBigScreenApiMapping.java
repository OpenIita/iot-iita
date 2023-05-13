package cc.iotkit.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "big_screen_api_mapping")
public class TbBigScreenApiMapping {

    @Id
    private String id;

    private String screenId;

    private String apiId;

}
