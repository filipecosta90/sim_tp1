caching = readtable('../response_time_caching.csv','ReadVariableNames',true);
no_caching = readtable('../response_time_no_caching.csv','ReadVariableNames',true);




hFig = figure(1);
set(hFig, 'Position', [0 0 920   920]);

plot(caching.requestTimeMs,'-','LineWidth',3);
hold on;
plot(no_caching.requestTimeMs,'-','LineWidth',3);


%ylim([ 0 20000 ]);
xlim([ 1 10 ]);
%set(gca,'YTickLabel',num2str(get(gca,'YTick').'));
ax = gca;
ax.XTick = [1 2 3 4 5 6 7 8 9 10];
ax.XTickLabel = {'Request 1', 'Request 2','Request 3','Request 4','Request 5','Request 6','Request 7','Request 8', 'Request 9', 'Request 10'};
legend('Com Caching','Sem caching');

 ylabel('Response Time (ms)');
 xlabel('Numero de HTTP Request');


t = title({'Analise do Response Time para HTTP Requests presentes ou ausentes no sistema de Web Caching'},'interpreter','latex')
set(t,'FontSize',18);

