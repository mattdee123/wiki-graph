import {Component} from "angular2/core";
import {ROUTER_DIRECTIVES} from "angular2/router";
import {CORE_DIRECTIVES} from "angular2/common";
import {UrlencodePipe} from "../pipes";
import {ElementRef} from "angular2/core";
import {QueryService} from "../query.service";

declare var $:any;
declare var $jit:any;

@Component({
  templateUrl: 'views/graph/graph.tpl.html',
  directives: [ROUTER_DIRECTIVES, CORE_DIRECTIVES],
  pipes: [UrlencodePipe]
})
export class GraphComponent {
  private maxArticles = 100;
  private maxDegree = 20;
  private maxDepth = 5;
  private page:string;

  private loading:boolean;
  private basePage:boolean;
  private error:boolean;
  private graph:any;

  constructor(private elementRef:ElementRef,
              private queryService:QueryService) {
  }

  private submit() {
    this.loading = true;
    this.graph = null;
    this.basePage = null;
    this.error = false;
    this.queryService.getGraph(this.page, this.maxDepth, this.maxDegree, this.maxArticles)
      .subscribe(
        res => {
          this.graph = res.json();
          this.basePage = this.graph.name;
          this.refreshGraph((node) => {});
          this.loading = false;
        },
        err => {
          this.error = true;
          this.loading = false;
        }
      );
  }

  private refreshGraph(onBeforeCompute) {
    $('#graph-canvaswidget').remove();

    if (!this.graph) {
      return;
    }

    let rGraph:any = new $jit.RGraph({
      injectInto: 'graph',
      background: {
        numberOfCircles: 100,
        CanvasStyles: {
          strokeStyle: '#555'
        }
      },
      Navigation: {
        enable: true,
        zooming: 10,
        panning: true
      },
      Node: {
        color: '#0099dd',
        dim: 3
      },
      Tips: {
        enable: true,
        type: 'Native',
        offsetX: 10,
        offsetY: 10,
        onShow: (tip, node) => {
          tip.innerHTML = node.name;
        }
      },
      Edge: {
        color: '#0099dd',
        lineWidth: 0.5
      },
      Events: {
        enable: true,
        type: 'Native',
        onClick: (node, eventInfo, e) => {
          if (!node || node.nodeFrom) {
            return;
          }
          rGraph.onClick(node.id, {
            duration: 400,
            transition: $jit.Trans.Quad.easeInOut
          });
        }
      },
      onPlaceLabel: (domElement, node) => {
        var style = domElement.style;
        style.display = '';
        style.cursor = 'pointer';

        if (node._depth === 1) {
          style.fontSize = "0.8em";
          style.color = "#ccc";
        } else {
          style.display = 'none';
        }

        var left = parseInt(style.left, 10);
        var w = domElement.offsetWidth;
        style.left = (left - w / 2) + 'px';
      },
      onBeforeCompute: onBeforeCompute
    });

    rGraph.loadJSON(this.graph);
    rGraph.graph.eachNode((n) => {
      n.getPos().setc(-200, -200);
    });
    rGraph.compute('end');
    rGraph.refresh();
    rGraph.canvas.scale(2, 2);
  }
}
