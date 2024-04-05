package insper.collie.squad;

public class SquadParser {

    public static Squad to(SquadInfo in) {
        return Squad.builder()
            .id(in.id())
            .name(in.name())
            .description(in.description())
            .company_id(in.company_id())
            .manager_id(in.manager_id())
            .build();
    }

    public static SquadInfo to(Squad in) {
        return SquadInfo.builder()
            .id(in.id())
            .name(in.name())
            .description(in.description())
            .company_id(in.company_id())
            .manager_id(in.manager_id())
            .build();
    }
    
}
