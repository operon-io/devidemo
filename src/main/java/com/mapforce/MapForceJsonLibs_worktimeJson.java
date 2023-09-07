package  com.mapforce;
import com.altova.json.*;
import com.altova.json.PropertyRule.NameMatchKind;
import com.altova.json.PropertyGroup.PropertyGroupBehavior;

public class MapForceJsonLibs_worktimeJson
{
    public static ValueAcceptor[] Schemas = 
    {
            new ValueAcceptor(
                "file:///C:/Users/35845/Documents/devikone-vagrant/demo/demo-mapforce-camel-yaml/src/main/resources/mapforce/worktimeJson.schema.json#/@32", 
                null, 
                null,
                null,
                null,
                new ArrayAcceptor(null, null, false, new Reference[]{new Reference("file:///C:/Users/35845/Documents/devikone-vagrant/demo/demo-mapforce-camel-yaml/src/main/resources/mapforce/worktimeJson.schema.json#//items/@64"), }),
                null,
                new AlsoAcceptor[]{}
            ),
            new ValueAcceptor(
                "file:///C:/Users/35845/Documents/devikone-vagrant/demo/demo-mapforce-camel-yaml/src/main/resources/mapforce/worktimeJson.schema.json#//items/@64", 
                null, 
                null,
                null,
                null,
                null,
                new ObjectAcceptor(null, null, new PropertyGroup[]{
                   new PropertyGroup(PropertyGroupBehavior.Succeed, PropertyGroupBehavior.Fail, PropertyGroupBehavior.ContinueWithNext, new PropertyRule[]{
                       new PropertyRule("person_id", NameMatchKind.Exact, new Reference("file:///C:/Users/35845/Documents/devikone-vagrant/demo/demo-mapforce-camel-yaml/src/main/resources/mapforce/worktimeJson.schema.json#//items//properties//person_id/@16"), null, new Reference("##fail")),
                       new PropertyRule("date", NameMatchKind.Exact, new Reference("file:///C:/Users/35845/Documents/devikone-vagrant/demo/demo-mapforce-camel-yaml/src/main/resources/mapforce/worktimeJson.schema.json#//items//properties//date/@16"), null, new Reference("##fail")),
                       new PropertyRule("arrival_ts", NameMatchKind.Exact, new Reference("file:///C:/Users/35845/Documents/devikone-vagrant/demo/demo-mapforce-camel-yaml/src/main/resources/mapforce/worktimeJson.schema.json#//items//properties//arrival_ts/@16"), null, new Reference("##fail")),
                       new PropertyRule("departure_ts", NameMatchKind.Exact, new Reference("file:///C:/Users/35845/Documents/devikone-vagrant/demo/demo-mapforce-camel-yaml/src/main/resources/mapforce/worktimeJson.schema.json#//items//properties//departure_ts/@16"), null, new Reference("##fail")),
                       new PropertyRule("extra_info", NameMatchKind.Exact, new Reference("file:///C:/Users/35845/Documents/devikone-vagrant/demo/demo-mapforce-camel-yaml/src/main/resources/mapforce/worktimeJson.schema.json#//items//properties//extra_info/@16"), null, new Reference("##fail")),
                       new PropertyRule("code", NameMatchKind.Exact, new Reference("file:///C:/Users/35845/Documents/devikone-vagrant/demo/demo-mapforce-camel-yaml/src/main/resources/mapforce/worktimeJson.schema.json#//items//properties//code/@16"), null, new Reference("##fail")),
                   } ),
                   new PropertyGroup(PropertyGroupBehavior.Succeed, PropertyGroupBehavior.Fail, PropertyGroupBehavior.ContinueWithNext, new PropertyRule[]{
                       new PropertyRule("", NameMatchKind.All, new Reference("##fail"), null, null),
                   } ),
                }),
                new AlsoAcceptor[]{}
            ),
            new ValueAcceptor(
                "file:///C:/Users/35845/Documents/devikone-vagrant/demo/demo-mapforce-camel-yaml/src/main/resources/mapforce/worktimeJson.schema.json#//items//properties//person_id/@16", 
                new StringAcceptor(null, null, null, null), 
                null,
                null,
                null,
                null,
                null,
                new AlsoAcceptor[]{}
            ),
            new ValueAcceptor(
                "file:///C:/Users/35845/Documents/devikone-vagrant/demo/demo-mapforce-camel-yaml/src/main/resources/mapforce/worktimeJson.schema.json#//items//properties//date/@16", 
                new StringAcceptor(null, null, null, null), 
                null,
                null,
                null,
                null,
                null,
                new AlsoAcceptor[]{}
            ),
            new ValueAcceptor(
                "file:///C:/Users/35845/Documents/devikone-vagrant/demo/demo-mapforce-camel-yaml/src/main/resources/mapforce/worktimeJson.schema.json#//items//properties//arrival_ts/@16", 
                new StringAcceptor(null, null, null, null), 
                null,
                null,
                null,
                null,
                null,
                new AlsoAcceptor[]{}
            ),
            new ValueAcceptor(
                "file:///C:/Users/35845/Documents/devikone-vagrant/demo/demo-mapforce-camel-yaml/src/main/resources/mapforce/worktimeJson.schema.json#//items//properties//departure_ts/@16", 
                new StringAcceptor(null, null, null, null), 
                null,
                null,
                null,
                null,
                null,
                new AlsoAcceptor[]{}
            ),
            new ValueAcceptor(
                "file:///C:/Users/35845/Documents/devikone-vagrant/demo/demo-mapforce-camel-yaml/src/main/resources/mapforce/worktimeJson.schema.json#//items//properties//extra_info/@16", 
                new StringAcceptor(null, null, null, null), 
                null,
                null,
                null,
                null,
                null,
                new AlsoAcceptor[]{}
            ),
            new ValueAcceptor(
                "file:///C:/Users/35845/Documents/devikone-vagrant/demo/demo-mapforce-camel-yaml/src/main/resources/mapforce/worktimeJson.schema.json#//items//properties//code/@16", 
                new StringAcceptor(null, null, null, null), 
                null,
                null,
                null,
                null,
                null,
                new AlsoAcceptor[]{}
            ),

    };
}    
