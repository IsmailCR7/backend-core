@param java.util.List<ru.mentee.power.crm.model.Lead> leads
@param ru.mentee.power.crm.model.LeadStatus currentFilter

@template.layout.main(content = @`
    <div class="bg-white rounded-lg shadow-md p-6">
        <!-- Заголовок с кнопкой добавления -->
        <div class="flex justify-between items-center mb-4">
            <h2 class="text-2xl font-bold">Список лидов</h2>

            <!-- НОВАЯ ЗЕЛЕНАЯ КНОПКА -->
            <a href="/leads/new"
               class="bg-green-500 hover:bg-green-600 text-white font-medium px-4 py-2 rounded-lg transition-colors duration-200 flex items-center gap-2">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd" d="M10 3a1 1 0 011 1v5h5a1 1 0 110 2h-5v5a1 1 0 11-2 0v-5H4a1 1 0 110-2h5V4a1 1 0 011-1z" clip-rule="evenodd" />
                </svg>
                Добавить лида
            </a>
        </div>

        <!-- Панель фильтрации (твоя существующая) -->
        <div class="mb-6">
            <h3 class="text-lg font-semibold mb-2">Фильтр по статусу:</h3>
            <div class="flex flex-wrap gap-2">
                <!-- Кнопка "Все" -->
                <a href="/leads"
                   class="px-4 py-2 rounded transition-colors inline-block
                          ${currentFilter == null ?
                            "bg-blue-600 text-white" :
                            "bg-gray-200 hover:bg-gray-300"}">
                    Все
                </a>

                <!-- Кнопка "NEW" -->
                <a href="/leads?status=NEW"
                   class="px-4 py-2 rounded transition-colors inline-block
                          ${currentFilter != null && currentFilter.toString() == "NEW" ?
                            "bg-blue-600 text-white" :
                            "bg-gray-200 hover:bg-gray-300"}">
                    Новые (NEW)
                </a>

                <!-- Кнопка "CONTACTED" -->
                <a href="/leads?status=CONTACTED"
                   class="px-4 py-2 rounded transition-colors inline-block
                          ${currentFilter != null && currentFilter.toString() == "CONTACTED" ?
                            "bg-blue-600 text-white" :
                            "bg-gray-200 hover:bg-gray-300"}">
                    В работе (CONTACTED)
                </a>

                <!-- Кнопка "QUALIFIED" -->
                <a href="/leads?status=QUALIFIED"
                   class="px-4 py-2 rounded transition-colors inline-block
                          ${currentFilter != null && currentFilter.toString() == "QUALIFIED" ?
                            "bg-blue-600 text-white" :
                            "bg-gray-200 hover:bg-gray-300"}">
                    Квалифицированные (QUALIFIED)
                </a>
            </div>

            <!-- Информация о текущем фильтре -->
            @if(currentFilter != null)
                <p class="mt-4 text-sm text-gray-600 bg-blue-50 border border-blue-200 rounded-lg p-3">
                    <span class="font-medium">Активный фильтр:</span>
                    показаны лиды со статусом
                    <span class="font-semibold">${currentFilter}</span>
                </p>
            @endif
        </div>

        @if(leads.isEmpty())
            <div class="bg-yellow-50 border border-yellow-200 rounded-lg p-4">
                <p class="text-yellow-600">Нет данных</p>
            </div>
        @else
            <table class="min-w-full bg-white border border-gray-200">
                <thead class="bg-gray-100">
                    <tr>
                        <th class="px-4 py-2 text-left">Email</th>
                        <th class="px-4 py-2 text-left">Company</th>
                        <th class="px-4 py-2 text-left">Status</th>
                        <th class="px-4 py-2 text-left">Действия</th>
                    </tr>
                </thead>
                <tbody>
                    @for(var lead : leads)
                        <tr class="border-t hover:bg-gray-50">
                            <td class="px-4 py-2">${lead.email()}</td>
                            <td class="px-4 py-2">${lead.company()}</td>
                            <td class="px-4 py-2">
                                @if(lead.status().toString() == "NEW")
                                    <span class="px-2 py-1 rounded text-sm bg-green-100 text-green-800">
                                        ${lead.status()}
                                    </span>
                                @elseif(lead.status().toString() == "CONTACTED")
                                    <span class="px-2 py-1 rounded text-sm bg-blue-100 text-blue-800">
                                        ${lead.status()}
                                    </span>
                                @else
                                    <span class="px-2 py-1 rounded text-sm bg-purple-100 text-purple-800">
                                        ${lead.status()}
                                    </span>
                                @endif
                            </td>
                            <td class="px-4 py-2">
                                <a href="/leads/${String.valueOf(lead.id())}/edit"
                                   class="bg-pink-100 hover:bg-pink-200 text-pink-700 px-3 py-1 rounded border border-pink-300">
                                    Редактировать
                                </a>
                                <form method="post" action="/leads/${String.valueOf(lead.id())}/delete"
                                     class="inline"
                                     onsubmit="return confirm('Удалить лида ${lead.company()}?')">
                                  <button type="submit"
                                         class="bg-red-600 text-white px-3 py-1 rounded hover:bg-red-700">
                                     Удалить
                                  </button>
                                </form>
                            </td>
                        </tr>
                    @endfor
                </tbody>
            </table>
        @endif
    </div>
`)