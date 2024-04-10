package insper.collie.squad;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@Tag(name = "Squad", description = "API do Squad")
public class SquadResource implements SquadController {

    @Autowired
    private SquadService squadService;

    @Override
    @Operation(summary = "Criar um novo Squad", description = "Cria um novo Squad e retorna o objeto criado com seu ID.",
        responses = {
            @ApiResponse(responseCode = "201", description = "Squad criado com sucesso", content = @Content(schema = @Schema(implementation = SquadInfo.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
        })
    public ResponseEntity<SquadInfo> create(@RequestBody(description = "Informações do Squad para a criação de um novo Squad.") SquadInfo in) {
        Squad squad = SquadParser.to(in);
        squad = squadService.create(squad);

        return ResponseEntity.created(
            ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(squad.id())
                .toUri())
            .body(SquadParser.to(squad));
    }

    @Override
    @Operation(summary = "Obter um Squad", description = "Obtém as informações de um Squad específico pelo seu ID.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Squad encontrado", content = @Content(schema = @Schema(implementation = SquadAllInfo.class))),
            @ApiResponse(responseCode = "404", description = "Squad não encontrado")
        })
    public ResponseEntity<SquadAllInfo> getSquad(@Parameter(description = "ID do Squad a ser obtido") String id){

        SquadAllInfo squad = squadService.getSquad(id);
        return ResponseEntity.ok(squad);
    }

    @Override
    @Operation(summary = "Verificar se o Squad existe", description = "Verifica se um squad específico existe pelo seu ID.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Squad encontrado", content = @Content(schema = @Schema(implementation = SquadAllInfo.class))),
        })
    public ResponseEntity<Boolean> isSquad(@Parameter(description = "ID do Squad a ser obtido") String id){

        Boolean isSquad = squadService.isSquad(id);
        return ResponseEntity.ok(isSquad);
    }

    @Override
    @Operation(summary = "Obter todos os Squads", description = "Obtém uma lista de todos os Squads.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de Squads encontrada", content = @Content(schema = @Schema(implementation = SquadInfo[].class)))
        })
    public ResponseEntity<List<SquadInfo>> getAllSquads(){

        List<Squad> squads = squadService.getAllSquads();
        return ResponseEntity.ok(
            squads.stream()
                .map(SquadParser::to)
                .collect(Collectors.toList())
        );
    }

    @Override
    @Operation(summary = "Atualizar um Squad", description = "Atualiza as informações de um Squad específico.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Squad atualizado com sucesso", content = @Content(schema = @Schema(implementation = SquadInfo.class))),
            @ApiResponse(responseCode = "404", description = "Squad não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
        })
    public ResponseEntity<SquadInfo> updateSquad(@Parameter(description = "ID do Squad a ser atualizado") String id, @RequestBody(description = "Informações atualizadas do Squad.") SquadInfo in){

        Squad squad = SquadParser.to(in);
        squad = squadService.update(id, squad);
        return ResponseEntity.ok(SquadParser.to(squad));
    }

    @Override
    @Operation(summary = "Deletar um Squad", description = "Deleta um Squad específico pelo seu ID.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Squad deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Squad não encontrado")
        })
    public ResponseEntity<SquadInfo> deleteSquad(@Parameter(description = "ID do Squad a ser deletado") String id){
        squadService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Override
    @Operation(summary = "Cadastrar Squads a partir de um CSV",
    description = "Cadastra múltiplos Squads a partir das informações contidas em um arquivo CSV.",
    responses = {
        @ApiResponse(responseCode = "200", description = "Squads cadastrados com sucesso", content = @Content(schema = @Schema(implementation = SquadInfo.class))),
        @ApiResponse(responseCode = "400", description = "Arquivo CSV inválido"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<List<SquadInfo>> registerSquadsFromCSV(@RequestParam("file") MultipartFile file) {
    if (file.isEmpty()) {
        return ResponseEntity.badRequest().build();
    }

    List<SquadInfo> registeredSquads = squadService.registerSquadsFromCSV(file);
    return ResponseEntity.ok(registeredSquads);
}
}
