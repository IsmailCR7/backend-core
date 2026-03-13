@param java.util.List<ru.mentee.power.crm.model.Lead> leads

@template.layout.main(content = @`
    <div class="bg-white rounded-lg shadow-md p-6">
        <h2 class="text-2xl font-bold mb-4">Список лидов</h2>

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
                        </tr>
                    @endfor
                </tbody>
            </table>
        @endif
    </div>
`)