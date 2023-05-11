package cc.iotkit.model.notify;

import cc.iotkit.model.Id;
import cc.iotkit.model.Owned;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author: 石恒
 * date: 2023-05-11 16:30
 * description:
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelTemplate implements Id<String> {

    private String id;
}
