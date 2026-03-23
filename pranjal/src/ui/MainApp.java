package ui;

import algorithms.BFS;
import algorithms.DFS;
import algorithms.Kruskal;
import algorithms.Dijkstra;
import graph.Graph;
import graph.GraphVisualData;
import step.Step;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;

public class MainApp extends Application {

    private static final double CANVAS_WIDTH = 750;
    private static final double CANVAS_HEIGHT = 550;
    private static final double STEP_DELAY = 0.6;

    private Pane canvas;
    private GraphRenderer renderer;
    private GraphVisualData visualData;
    private Graph graph;
    private StepAnimator animator;

    private Label stepLabel;
    private Label stepInfoLabel;
    private Button btnPlayPause;

    @Override
    public void start(Stage stage) {

        // prepare graph frmo data
        graph = buildSampleGraph();
        visualData = new GraphVisualData();
        visualData.buildFromGraph(graph, CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2, 190);

        // canvas
        canvas = new Pane();
        canvas.setPrefSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        canvas.setStyle("-fx-background-color: #ecf0f1;");

        // render graph
        renderer = new GraphRenderer(canvas);
        renderer.render(visualData);

        // animator
        animator = new StepAnimator(renderer, STEP_DELAY);
        animator.setVisualData(visualData);
        animator.setOnStepChange(this::updateStepLabel);

        // header
        Label title = new Label("Network Optimizer and Visualizer");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        title.setStyle("-fx-text-fill: white;");
        HBox header = new HBox(title);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(12));
        header.setStyle("-fx-background-color: #34495e;");

        VBox legend = buildLegend();

        Button btnBFS = styledButton("▶ BFS", "#3498db", "#2980b9");
        Button btnDFS = styledButton("▶ DFS", "#3498db", "#2980b9");
        Button btnKruskal = styledButton("▶ Kruskal", "#3498db", "#2980b9");
        Button btnDijkstra = styledButton("▶ Dijkstra", "#3498db", "#3498db");
        Button btnReset = styledButton("↺ Reset", "#7f8c8d", "#636e72");

        btnBFS.setOnAction(e -> startAnimation(BFS.run(graph, 0)));
        btnDFS.setOnAction(e -> startAnimation(DFS.run(graph, 0)));
        btnKruskal.setOnAction(e -> startAnimation(Kruskal.run(graph)));
        btnDijkstra.setOnAction(e -> startAnimation(Dijkstra.run(graph, 0)));
        btnReset.setOnAction(e -> resetAll());

        HBox algoBar = new HBox(12, btnBFS, btnDFS, btnKruskal, btnDijkstra, btnReset);
        algoBar.setAlignment(Pos.CENTER);
        algoBar.setPadding(new Insets(10, 0, 4, 0));

        Button btnBack = styledButton("⏮ Back", "#8e44ad", "#6c3483");
        btnPlayPause = styledButton("⏯ Play", "#27ae60", "#1e8449");
        Button btnForward = styledButton("⏭ Next", "#8e44ad", "#6c3483");

        btnBack.setOnAction(e -> animator.stepBackward());
        btnForward.setOnAction(e -> animator.stepForward());
        btnPlayPause.setOnAction(e -> togglePlayPause());

        stepLabel = new Label("Step: 0 / 0");
        stepLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        stepLabel.setStyle("-fx-text-fill: #ecf0f1;");

        stepInfoLabel = new Label("");
        stepInfoLabel.setFont(Font.font("Arial", 13));
        stepInfoLabel.setStyle("-fx-text-fill: #bdc3c7;");

        VBox stepInfo = new VBox(2, stepLabel, stepInfoLabel);
        stepInfo.setAlignment(Pos.CENTER_LEFT);

        HBox playbackBar = new HBox(12, btnBack, btnPlayPause, btnForward, stepInfo);
        playbackBar.setAlignment(Pos.CENTER);
        playbackBar.setPadding(new Insets(4, 0, 10, 0));

        VBox bottomPanel = new VBox(algoBar, playbackBar);
        bottomPanel.setAlignment(Pos.CENTER);
        bottomPanel.setStyle("-fx-background-color: #2c3e50;");
        bottomPanel.setPadding(new Insets(6, 12, 8, 12));

        BorderPane root = new BorderPane();
        root.setTop(header);
        root.setCenter(canvas);
        root.setRight(legend);
        root.setBottom(bottomPanel);

        Scene scene = new Scene(root);
        stage.setTitle("Network Optimizer and Visualizer");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void startAnimation(List<Step> steps) {
        resetAll();
        animator.load(steps);
        animator.play();
        btnPlayPause.setText("⏸ Pause");
    }

    private void resetAll() {
        animator.stop();
        animator.load(List.of()); // clear steps
        renderer.render(visualData);
        btnPlayPause.setText("⏯ Play");
        stepLabel.setText("Step: 0 / 0");
        stepInfoLabel.setText("");
    }

    private void togglePlayPause() {
        if (animator.isPlaying()) {
            animator.pause();
            btnPlayPause.setText("⏯ Play");
        } else {

            animator.play();
            btnPlayPause.setText("⏸ Pause");
        }
    }

    private void updateStepLabel() {
        int cur = animator.getCurrentIndex() + 1;
        int total = animator.getTotalSteps();
        stepLabel.setText("Step: " + cur + " / " + total);

        // Show description of the current step
        if (animator.getCurrentIndex() >= 0 && animator.getCurrentIndex() < total) {
            stepInfoLabel.setText("  " + animator.getCurrentStep().toString());
        }
    }

    private VBox buildLegend() {
        VBox box = new VBox(8);
        box.setPadding(new Insets(16, 14, 16, 14));
        box.setStyle("-fx-background-color: #2c3e50; -fx-background-radius: 0;");
        box.setPrefWidth(170);

        Label heading = new Label("Legend");
        heading.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        heading.setStyle("-fx-text-fill: white;");
        box.getChildren().add(heading);

        // ── Node colours ──
        Label nodeHeading = new Label("Nodes");
        nodeHeading.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        nodeHeading.setStyle("-fx-text-fill: #bdc3c7;");
        nodeHeading.setPadding(new Insets(8, 0, 0, 0));
        box.getChildren().add(nodeHeading);

        box.getChildren().add(legendItem(Color.web("#3498db"), "Default"));
        box.getChildren().add(legendItem(Color.web("#f39c12"), "In Queue"));
        box.getChildren().add(legendItem(Color.web("#e74c3c"), "Visiting"));
        box.getChildren().add(legendItem(Color.web("#2ecc71"), "Processed"));

        // ── Edge colours ──
        Label edgeHeading = new Label("Edges");
        edgeHeading.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        edgeHeading.setStyle("-fx-text-fill: #bdc3c7;");
        edgeHeading.setPadding(new Insets(10, 0, 0, 0));
        box.getChildren().add(edgeHeading);

        box.getChildren().add(legendLine(Color.web("#95a5a6"), "Default"));
        box.getChildren().add(legendLine(Color.web("#e67e22"), "Exploring"));
        box.getChildren().add(legendLine(Color.web("#f1c40f"), "Considered"));
        box.getChildren().add(legendLine(Color.web("#27ae60"), "Selected (MST)"));
        box.getChildren().add(legendLine(Color.web("#c0392b"), "Rejected"));

        return box;
    }

    private HBox legendItem(Color color, String text) {
        Circle dot = new Circle(7, color);
        dot.setStroke(Color.web("#2c3e50"));
        dot.setStrokeWidth(1.5);
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Arial", 12));
        lbl.setStyle("-fx-text-fill: #ecf0f1;");
        HBox row = new HBox(8, dot, lbl);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private HBox legendLine(Color color, String text) {
        Line line = new Line(0, 0, 20, 0);
        line.setStroke(color);
        line.setStrokeWidth(3);
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Arial", 12));
        lbl.setStyle("-fx-text-fill: #ecf0f1;");
        HBox row = new HBox(8, line, lbl);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private Graph buildSampleGraph() {
        Graph g = new Graph(false);
        g.addWeightedEdge(0, 1, 4);
        g.addWeightedEdge(0, 3, 11);
        g.addWeightedEdge(1, 2, 8);
        g.addWeightedEdge(1, 4, 2);
        g.addWeightedEdge(2, 5, 7);
        g.addWeightedEdge(3, 4, 9);
        g.addWeightedEdge(4, 5, 6);
        return g;
    }

    private Button styledButton(String text, String bg, String hoverBg) {
        String base = "-fx-background-color: " + bg + "; -fx-text-fill: white; "
                + "-fx-padding: 8 16; -fx-background-radius: 5; -fx-cursor: hand;";
        String hover = "-fx-background-color: " + hoverBg + "; -fx-text-fill: white; "
                + "-fx-padding: 8 16; -fx-background-radius: 5; -fx-cursor: hand;";
        Button btn = new Button(text);
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        btn.setStyle(base);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e -> btn.setStyle(base));
        return btn;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
